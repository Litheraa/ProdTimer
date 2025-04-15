package litheraa.controller;

import com.github.weisj.darklaf.LafManager;
import litheraa.data.entities.Prod;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data.models.CalendarModel;
import litheraa.util.CalendarWrapper;
import litheraa.util.ViewType;
import litheraa.view.*;
import litheraa.view.calendar.*;
import litheraa.view.calendar.calendar_settings.CalendarSettings;
import litheraa.view.table.ColumnController;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.ThemeSupplier;
import litheraa.view.util.fabric.FontFactory;
import litheraa.view.util.fabric.IconFactory;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.time.YearMonth;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CalendarController implements ViewControllerInterface {
	@lombok.Getter
	private final MainFrame frame;
	@lombok.Getter
	private ColumnController columnController;
	private final ProdTimerController controller;
	private final LocalDate period = SettingsController.getPeriod();
	private final CalendarModel model;

	public CalendarController(ProdTimerController controller, MainFrame frame, ViewType type) {
		LafManager.installTheme(ThemeSupplier.getTheme());

		this.controller = controller;
		this.model = new CalendarModel(controller.getData(), Long.decode(SettingsController.getText()));
		this.frame = frame;

		this.frame.setLocation(SettingsController.getLocation());
		concreteView(type);
		this.frame.setSize(SettingsController.getSize(type.ordinal()));
		this.frame.setVisible(true);
	}

	private String getTextName() {
		return model.getTextName();
	}

	@Override
	public void concreteView(ViewType type) {
		CalendarPanel panel;
		AdjustablePanel header;
		switch (type) {
			case WEEKLY -> {
				frame.setMinimumSize(new Dimension(610, 215));
				panel = new WeeklyCalendarPanel(this, period, model);
				header = createHeader((period.getDayOfMonth() / 7) + 1
						+ " неделя " + CalendarWrapper.localeRu(YearMonth.of(period.getYear(), period.getMonth())));
//				TODO отнимать высоту в лишних строках
			}
			case DAILY -> {
				frame.setMinimumSize(new Dimension(140, 165));
				panel = new DailyCalendarPanel(this, period, model);
				header = createHeader(CalendarWrapper.localeRu(period));
			}
			default -> {
				frame.setMinimumSize(new Dimension(632, 580));
				panel = new MonthlyCalendarPanel(this, period, model);
				;
				header = createHeader(CalendarWrapper.localeRu(YearMonth.of(period.getYear(), period.getMonth())));
			}
		}

		frame.setSize(SettingsController.getSize(type.ordinal()));
		frame.setHeader(header);
		frame.setMainComponent(panel);
//		TODO разные значения SSA для разных типов вида
		frame.addComponentListener(new SizeStepAdapter(700, 125, panel, header));
		frame.pack();
	}

	private AdjustablePanel createHeader(String headerText) {
		String finalText = headerText + " : " + getTextName();
		JLabel header = new JLabel(finalText) {
			@Override
			public void setText(String text) {
				super.setText(text.substring(0, 1).toUpperCase() + text.substring(1));
			}
		};
		header.setHorizontalAlignment(SwingConstants.CENTER);

		JDialog dialog = new CalendarSettings(this, finalText, model.getTextNames());

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
		panel.setBackground(ThemeSupplier.getThemeColor().getAccentBackground());

		return panel;
	}

	@Override
	public void setTextId(String textId) {
		model.setTextId(textId);
	}

	@Override
	public Pair<List<Time>, List<Text>> getModel() {
		return controller.getData();
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
}
