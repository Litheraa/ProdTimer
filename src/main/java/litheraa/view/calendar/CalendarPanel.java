package litheraa.view.calendar;

import litheraa.controller.CalendarController;
import litheraa.data.models.CalendarModel;
import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.factory.FontFactory;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import java.awt.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public abstract class CalendarPanel extends JPanel implements AdjustableComponentInterface {
	protected final FontFactory fontFactory = FontFactory.getInstance();
	protected final CalendarModel calendarModel;
	protected final CalendarController controller;
	private final Map<LocalDate, AdjustableComponentInterface> dayPanels = new HashMap<>(31);

	protected CalendarPanel(CalendarController controller, CalendarModel calendarModel) {
		this.controller = controller;
		this.calendarModel = calendarModel;
	}

	protected final void build(){
		boolean firstDayPanel = true;
		JPanel dayGrid = createGrid();

		for (LocalDate id = getStart(); id.isBefore(getEnd().plusDays(1L)); id = id.plusDays(1L)) {
			AdjustablePanel adjustablePanel = AdjustablePanel.dayPanelbuilder(id)
					.dayLabel()
					.labelGroup(calendarModel.getWritten(id), calendarModel.getGoal(id))
					.progressBar(calendarModel.getWritten(id), calendarModel.getGoal(id))
					.build();
			if (firstDayPanel) {
				adjustablePanel.addComponentListener(new AspectRatioAdapter(this));
				firstDayPanel = false;
			}
			dayGrid.add(adjustablePanel);
			dayPanels.put(id, adjustablePanel);
		}

		AdjustablePanel subHeader = createSubHeader();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, subHeader, 1, SpringLayout.NORTH, this);
		layout.putConstraint(SpringLayout.WIDTH, subHeader, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.WIDTH, dayGrid, 0, SpringLayout.WIDTH, this);
		layout.putConstraint(SpringLayout.NORTH, dayGrid, 1, SpringLayout.SOUTH, subHeader);
		layout.putConstraint(SpringLayout.SOUTH, dayGrid, -1, SpringLayout.SOUTH, this);

		setLayout(layout);
		add(subHeader);
		add(dayGrid);

		dayPanels.put(subHeader.getId(), subHeader);
	}

	protected AdjustablePanel createSubHeader() {
		JLabel[] labels = new JLabel[7];
		for (DayOfWeek day : DayOfWeek.values()) {
			JLabel label = new JLabel(day.getDisplayName(TextStyle.FULL_STANDALONE, Locale.of("ru")));
			label.setOpaque(true);
			label.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
			label.setBackground(UIManager.getColor("SubTitle.background"));
			label.setHorizontalAlignment(JLabel.CENTER);
			labels[day.getValue() - 1] = label;
		}

		return AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 2))
				.layout(new GridLayout(1, 7, 5, 0))
				.label((l, step) ->
						l.setFont(fontFactory.getFont("subHeader", step, 4, Font.PLAIN)), labels)
				.build();
	}

	abstract JPanel createGrid();

	abstract void setGoal(int goal, Long timeId);

	abstract LocalDate getStart();

	abstract LocalDate getEnd();

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
}
