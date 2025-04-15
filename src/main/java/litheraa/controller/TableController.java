package litheraa.controller;

import com.github.weisj.darklaf.LafManager;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data.models.CalendarModel;
import litheraa.data.models.TextModel;
import litheraa.util.ViewType;
import litheraa.view.MainFrame;
import litheraa.view.calendar.*;
import litheraa.view.calendar.calendar_settings.CalendarSettings;
import litheraa.view.table.ColumnController;
import litheraa.view.table.ProdTimerTable;
import litheraa.view.table.TimeTableModel;
import litheraa.view.table.TextTableModel;
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
import java.util.List;
import java.util.Objects;

public class TableController implements ViewControllerInterface {
	@lombok.Getter
	private ColumnController columnController;
	@lombok.Getter
	private final MainFrame frame;
	private ProdTimerTable table;
	private final ProdTimerController controller;
	private final Pair<List<Time>, List<Text>> data;

	public TableController(ProdTimerController controller, MainFrame frame, ViewType type) {
		LafManager.installTheme(ThemeSupplier.getTheme());

		this.controller = controller;
		this.data = controller.getData();
		this.frame = frame;

		concreteView(type);
		this.frame.setLocation(SettingsController.getLocation());
		this.frame.setResizable(true);
		this.frame.setVisible(true);
	}

	@Override
	public void concreteView(ViewType type) {
		AdjustablePanel header;
		if (Objects.requireNonNull(type) == ViewType.TEXTS) {
			table = new ProdTimerTable(frame, new TextTableModel(new TextModel(data, Long.decode(SettingsController.getText()))));
			table.setProgress();
			header = createHeader(type.getLocale());

			columnController = new ColumnController(table);
			columnController.adjustColumns();
			frame.setResizable(true);
		} else {
			table = new ProdTimerTable(frame, new TimeTableModel(new CalendarModel(controller.getData(), Long.decode(SettingsController.getText()))));
			table.setTextArea();
			table.setProgressBar();
			header = createHeader(type.getLocale());

			columnController = new ColumnController(table);
			columnController.adjustColumns();
		}

		frame.setSize(SettingsController.getSize(type.ordinal()));
		frame.setHeader(header);
		frame.setMainComponent(new JScrollPane(table));
		frame.addComponentListener(new SizeStepAdapter(700, 125, header));
		frame.pack();
	}

	private AdjustablePanel createHeader(String headerText) {
		String finalText = headerText + " : " + getTextName();
		JLabel header = new JLabel(finalText);
		header.setHorizontalAlignment(SwingConstants.CENTER);

		JDialog dialog = new CalendarSettings(this, finalText,
				data.getSecond().stream().map(Text::getName).toArray(String[]::new));

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

	private String getTextName() {
		return data.getSecond()
				.stream()
				.filter(t -> Objects.equals(t.getId(), Long.decode(SettingsController.getText())))
				.findFirst()
				.orElse(new Text("Все тексты"))
				.getName();
	}

	public void pinTable(boolean flag) {
		table.pinElements(flag);
	}

	public void saveColumnPositions() {
		columnController.saveColumnPositions();
	}

	@Override
	public void setTextId(String textId) {

	}

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
		if (SettingsController.getViewType().ordinal() <= 1) {
			columnController.adjustColumns();
		}
		repaint();
	}

	@Override
	public void setGoal(int goal, Long... timeId) {

	}

	@Override
	public void reset() {

	}
}
