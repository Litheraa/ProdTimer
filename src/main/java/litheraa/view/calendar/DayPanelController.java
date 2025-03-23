package litheraa.view.calendar;

import litheraa.controller.ViewController;
import litheraa.data.models.ProdTimeModel;
import litheraa.view.themes.ThemeColors;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.*;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.*;

public class DayPanelController implements AdjustableComponentInterface {
	private final ProdTimeModel prodTimeModel;
	private final Map<LocalDate, AdjustableComponentInterface> dayPanels = new HashMap<>(31);
	private final FontFactory fontFactory = FontFactory.getInstance();
	private final IconFactory iconFactory = IconFactory.getInstance();

	public DayPanelController(ProdTimeModel prodTimeModel) {
		this.prodTimeModel = prodTimeModel;
	}

	private AdjustablePanel createHeader(String headerText, String headerIcon) {
		JLabel label = new JLabel(headerText.substring(0, 1).toUpperCase() + headerText.substring(1));
		label.setHorizontalAlignment(SwingConstants.CENTER);

		JLabel icon = new JLabel("");
		icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));

		AdjustablePanel panel = AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 1))
				.label((l, step) -> l.setFont(fontFactory
						.getFont("header", step, 8, Font.BOLD)), new Pair<>(label, BorderLayout.CENTER))
				.label((l, step) -> l.setIcon(iconFactory.getIcon(headerIcon, step, 8)), new Pair<>(icon, BorderLayout.EAST))
				.layout(new BorderLayout())
				.build();

		panel.add(label, BorderLayout.CENTER);
		panel.add(icon, BorderLayout.EAST);

		panel.setBackground(((ThemeColors) ViewController.getTheme()).getAccentBackground());
		return panel;
	}

	private AdjustablePanel createSubHeader() {
		JLabel[] labels = new JLabel[7];
		for (DayOfWeek day : DayOfWeek.values()) {
			JLabel label = new JLabel(day.getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")));
			label.setOpaque(true);
			label.setForeground(((ThemeColors) ViewController.getTheme()).getBackgroundDark());
			label.setBackground(((ThemeColors) ViewController.getTheme()).getForeground());
			label.setHorizontalAlignment(JLabel.CENTER);
			labels[day.getValue() - 1] = label;
		}

		return AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 2))
				.layout(new GridLayout(1, 7, 5, 0))
				.label((l, step) ->
						l.setFont(fontFactory.getFont("subHeader", step, 4, Font.PLAIN)), labels).build();
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
			AdjustablePanel adjustablePanel = AdjustablePanel.dayPanelbuilder(iterator.getId())
					.dayLabel()
					.labelGroup(iterator.getWritten(), iterator.getGoal())
					.progressBar(iterator.getWritten(), iterator.getGoal())
					.build();
			if (firstDayPanel) {
				adjustablePanel.addSizeStepListener(this);
				adjustablePanel.addComponentListener(new AspectRatioAdapter(this));
				firstDayPanel = false;
			}
			dayGrid.add(adjustablePanel);
			dayPanels.put(iterator.getId(), adjustablePanel);
			iterator.next();
		}
		JPanel panel = new JPanel();
		AdjustablePanel header = createHeader(prodTimeModel.getMonth().getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")), "gear.png");
		AdjustablePanel subHeader = createSubHeader();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WIDTH, header, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.NORTH, header, 0, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.NORTH, subHeader, 1, SpringLayout.SOUTH, header);
		layout.putConstraint(SpringLayout.WIDTH, subHeader, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.WIDTH, dayGrid, 0, SpringLayout.WIDTH, panel);
		layout.putConstraint(SpringLayout.NORTH, dayGrid, 1, SpringLayout.SOUTH, subHeader);
		layout.putConstraint(SpringLayout.SOUTH, dayGrid, 0, SpringLayout.SOUTH, panel);

		panel.setLayout(layout);

		panel.add(header);
		panel.add(subHeader);
		panel.add(dayGrid);

		dayPanels.put(header.getId(), header);
		dayPanels.put(subHeader.getId(), subHeader);
		return panel;
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		dayPanels.values().forEach(dayPanel -> dayPanel.aspectRatioChanged(ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		dayPanels.values().forEach(dayPanel -> dayPanel.sizeChanged(step));
	}

	@Override
	public JComponent setParent(JComponent parent) {
		return null;
	}

	public void setGoal(int goal, Long timeId) {

	}


}
