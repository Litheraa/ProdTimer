package litheraa.view.calendar;

import litheraa.controller.ViewController;
import litheraa.data.models.ProdTimeModel;
import litheraa.view.themes.ThemeColors;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.*;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class DayPanelController implements AdjustableComponentInterface {
	private final ProdTimeModel prodTimeModel;
	private final Map<LocalDate, AdjustableComponentInterface> dayPanels = new HashMap<>(31);
	private final ConstraintFactory constraintFactory = new ConstraintFactory();
	private final DimensionLightWeight dimensionLightWeight = new DimensionLightWeight();
	private final FontLightWeight fontLightWeight = new FontLightWeight();
	private final IconFactory iconFactory = new IconFactory();

	public DayPanelController(ProdTimeModel prodTimeModel) {
		this.prodTimeModel = prodTimeModel;
	}

	private DayPanel createHeader(String headerText, String headerIcon) {
		JLabel label = new JLabel(headerText.substring(0, 1).toUpperCase() + headerText.substring(1));
		JLabel icon = new JLabel("");

		DayPanel panel = DayPanel.builder(LocalDate.of(1970, 1, 1), fontLightWeight)
				.label(label, (l, step) -> l.setFont(fontLightWeight
						.getFont("header", step, 10, Font.BOLD)))
				.label(icon, (l, step) -> l.setIcon(iconFactory.getIcon(headerIcon, step, 10)))
				.build();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.EAST, icon, -5, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, icon, 0, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, panel);

		panel.setLayout(layout);
		panel.setBackground(((ThemeColors) ViewController.getTheme()).getAccentBackground());
		return panel;
	}

	private JLabel createLabel(String headerName, JPanel parent) {
		JLabel label = new JLabel(headerName.substring(0, 1).toUpperCase() + headerName.substring(1));
		label.setOpaque(true);
//		label.setBackground(background);

//		AdjustableLabelContainer<JLabel> headerLabelContainer = new AdjustableLabelContainer<>(label, (c, step)-> label
//				.setFont(fontLightWeight
//						.getFont("header", step, 10, Font.BOLD)));
//		parent.add(label);
//	dayPanels.put(LocalDate.of(1970, 1, 1), headerLabelContainer);

		return label;
	}

	private JLabel createIconLabel(String iconName, int dayNo) {
		JLabel icon = new JLabel();
//		AdjustableIconContainer<JLabel> iconContainer = new AdjustableIconContainer<>(icon, iconName, iconFactory);
//		dayPanels.put(LocalDate.of(1970, 1, dayNo), iconContainer);
		return icon;
	}

	private DayPanel createSubHeader() {
		DayPanel dayNamesPanel = DayPanel.builder(LocalDate.of(1970, 1, 2), fontLightWeight)
				.dayName(DayOfWeek.of(1).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.dayName(DayOfWeek.of(2).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.dayName(DayOfWeek.of(3).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.dayName(DayOfWeek.of(4).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.dayName(DayOfWeek.of(5).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.dayName(DayOfWeek.of(6).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.dayName(DayOfWeek.of(7).getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")))
				.build();

		dayNamesPanel.setLayout(new GridLayout(1, 7, 5, 0));

		return dayNamesPanel;
	}

	private JPanel createTweakedPanel(JLabel label, JLabel icon, Color background) {
		JPanel panel = new JPanel();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.EAST, icon, -5, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.VERTICAL_CENTER, icon, 0, SpringLayout.VERTICAL_CENTER, panel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, label, 0, SpringLayout.HORIZONTAL_CENTER, panel);

		panel.setLayout(layout);
		panel.add(label);
		panel.add(icon);
		panel.setBackground(background);
		return panel;
	}

	private CalendarGrid createGrid() {
		return new CalendarGrid(prodTimeModel.getWeeks(), 5, 5,
				prodTimeModel.getFirstDay(), prodTimeModel.getLastDay());
	}

	public JPanel fullDayPanel() {
		boolean firstDayPanel = true;

		JPanel dayGrid = createGrid();

		ProdTimeModel.Iterator iterator = prodTimeModel.iterator();
		while (iterator.hasNext()) {
			DayPanel dayPanel;
			if (firstDayPanel) {
				dayPanel = DayPanel.builder(iterator.getId(), fontLightWeight, constraintFactory, dimensionLightWeight, iconFactory)
						.dayLabel()
						.labelGroup(iterator.getWritten(), iterator.getGoal())
						.progressBar(iterator.getWritten(), iterator.getGoal())
						.sizeStepListener(this)
						.build();
				dayPanel.addComponentListener(new AspectRatioAdapter(this));
				firstDayPanel = false;
			} else {
				dayPanel = DayPanel.builder(iterator.getId(), fontLightWeight, constraintFactory, dimensionLightWeight, iconFactory)
						.dayLabel()
						.labelGroup(iterator.getWritten(), iterator.getGoal())
						.progressBar(iterator.getWritten(), iterator.getGoal())
						.build();
			}
			dayGrid.add(dayPanel);
			dayPanels.put(iterator.getId(), dayPanel);
			iterator.next();
		}
		Color background = ((ThemeColors) ViewController.getTheme()).getAccentBackground();
		JPanel panel = new JPanel();
		DayPanel header = createHeader(prodTimeModel.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")), "gear.png");
		DayPanel subHeader = createSubHeader();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.EAST, header, 0, SpringLayout.EAST, panel);
		layout.putConstraint(SpringLayout.WIDTH, header, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.NORTH, header, 0, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.SOUTH, header, 35, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.NORTH, subHeader, 1, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.SOUTH, subHeader, 25, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.WIDTH, subHeader, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.WIDTH, dayGrid, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.NORTH, dayGrid, 1, SpringLayout.SOUTH, subHeader);
		layout.putConstraint(SpringLayout.SOUTH, dayGrid, 0, SpringLayout.SOUTH, panel);
		panel.setLayout(layout);

		panel.add(header);
		panel.add(subHeader);
		panel.add(dayGrid);

		dayPanels.put(LocalDate.of(1970, 1, 1), header);
		dayPanels.put(LocalDate.of(1970, 1, 2), subHeader);
		return panel;
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		for (AdjustableComponentInterface dayPanel : dayPanels.values()) {
			dayPanel.aspectRatioChanged(ratio);
		}
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		for (AdjustableComponentInterface dayPanel : dayPanels.values()) {
			dayPanel.sizeChanged(step);
		}
	}

	@Override
	public void wireWithParent(JComponent parent) {
	}

	public void setGoal(int goal, Long timeId) {

	}


}
