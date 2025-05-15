package litheraa.controller;

import litheraa.data.entities.SelectableText;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data.models.CalendarModel;
import litheraa.settings.DBSettings;
import litheraa.settings.SettingsManager;
import litheraa.util.CalendarWrapper;
import litheraa.util.ViewType;
import litheraa.view.*;
import litheraa.view.calendar.*;
import litheraa.view.calendar.calendar_settings.CalendarSettings;
import litheraa.view.table.ColumnController;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.factory.FontFactory;
import litheraa.view.util.factory.IconFactory;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class CalendarController implements ViewControllerInterface {
	@Getter
	private final MainFrame frame;
	@Getter
	private ColumnController columnController;
	private final ProdTimerController controller;
	private final CalendarModel model;
	private final Pair<List<Time>, ArrayList<SelectableText>> data;
	@Getter
	private List<SelectableText> texts;

	public CalendarController(ProdTimerController controller, MainFrame frame, ViewType type) {
		this.controller = controller;
		this.data = controller.getData();
		texts = data.getSecond();
		this.model = new CalendarModel(data);
		this.frame = frame;

		concreteView(type, controller.getDbSettings().getPeriod().getFrom());
		this.frame.setVisible(true);
	}

	@Override
	public void concreteView(ViewType type, LocalDate period) {
		CalendarPanel panel;
		AdjustablePanel header;
		switch (type) {
			case WEEKLY -> {
				frame.setMinimumSize(new Dimension(610, 215));
				panel = new WeeklyCalendarPanel(this, period, model);
				header = createHeader((period.getDayOfMonth() / 7) + 1
						+ " неделя " + CalendarWrapper.localeRu(YearMonth.of(period.getYear(), period.getMonth())), data.getSecond());
//				TODO отнимать высоту в лишних строках
				SettingsManager.loadSettings(type, frame);
			}
			case DAILY -> {
				frame.setMinimumSize(new Dimension(140, 165));
				panel = new DailyCalendarPanel(this, period, model);
				header = createHeader(CalendarWrapper.localeRu(period), data.getSecond());
				SettingsManager.loadSettings(type, frame);
			}
			default -> {
				frame.setMinimumSize(new Dimension(632, 580));
				panel = new MonthlyCalendarPanel(this, period, model);
				header = createHeader(CalendarWrapper.localeRu(YearMonth.of(period.getYear(), period.getMonth())), data.getSecond());
				SettingsManager.loadSettings(type, frame);
			}
		}

		frame.setHeader(header);
		frame.setMainComponent(panel);
//		TODO разные значения SSA для разных типов вида
		frame.addComponentListener(new SizeStepAdapter(700, 125, panel, header));
	}

	private AdjustablePanel createHeader(String headerText, ArrayList<SelectableText> textList) {
		String finalText = headerText + " : " + SelectableText.getTextNamePresentation(textList);
		JLabel header = new JLabel(finalText) {
			@Override
			public void setText(String text) {
				super.setText(text.substring(0, 1).toUpperCase() + text.substring(1));
			}
		};
		header.setHorizontalAlignment(SwingConstants.CENTER);

		JDialog dialog = new CalendarSettings(this, finalText, textList);

		JLabel icon = new JLabel("");
		icon.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 5));
		icon.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!dialog.isVisible()) {
					Point p = e.getPoint();
					SwingUtilities.convertPointToScreen(p, icon);
					dialog.setLocation(p);
					dialog.setVisible(true);
				} else {
					dialog.setVisible(false);
				}
			}
		});

		AdjustablePanel panel = AdjustablePanel.adjustablePanelBuilder(LocalDate.of(1970, 1, 1))
				.label((l, step) -> l.setFont(FontFactory.getInstance()
						.getFont("header", step, 8, Font.BOLD)), header, BorderLayout.CENTER)
				.label((l, step) -> l.setIcon(IconFactory.getInstance()
						.getIcon("gear.png", step, 8)), icon, BorderLayout.EAST)
				.layout(new BorderLayout())
				.build();

		panel.add(header, BorderLayout.CENTER);
		panel.add(icon, BorderLayout.EAST);

		return panel;
	}

	@Override
	public void setTextId(List<SelectableText> textId) {
//		data.getSecond().forEach(sT -> s);
	}

	@Override
	public List<SelectableText> getSelectableTexts() {
		System.out.println(data.getSecond());
		return data.getSecond();
	}

	@Override
	public Pair<List<Time>, List<Text>> getModel() {
		return null;
	}

	@Override
	public void repaint() {
		frame.setVisible(false);
		frame.setVisible(true);
	}

	@Override
	public void refresh() {
		repaint();
	}

	@Override
	public void setGoal(int goal, Long... timeId) {
		controller.setGoal(goal, timeId);
	}

	public Set<Integer> getUniqueYears() {
		return controller.getUniqueYears();
	}

	@Override
	public void reset() {
		controller.reset();
	}

	@Override
	public DBSettings getSettings() {
		return controller.getDbSettings();
	}
}
