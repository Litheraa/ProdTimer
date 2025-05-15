package litheraa.controller;

import litheraa.data.entities.SelectableText;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.data.models.CalendarModel;
import litheraa.data.models.TextModel;
import litheraa.settings.DBSettings;
import litheraa.settings.SettingsManager;
import litheraa.util.ViewType;
import litheraa.view.MainFrame;
import litheraa.view.calendar.*;
import litheraa.view.calendar.calendar_settings.CalendarSettings;
import litheraa.view.table.ColumnController;
import litheraa.view.table.ProdTimerTable;
import litheraa.view.table.TimeTableModel;
import litheraa.view.table.TextTableModel;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.factory.FontFactory;
import litheraa.view.util.factory.IconFactory;
import org.apache.commons.math3.util.Pair;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class TableController implements ViewControllerInterface {
	@lombok.Getter
	private ColumnController columnController;
	@lombok.Getter
	private final MainFrame frame;
	private ProdTimerTable table;
	private final ProdTimerController controller;
	private final Pair<List<Time>, ArrayList<SelectableText>> data;

	public TableController(ProdTimerController controller, MainFrame frame, ViewType type) {
		this.controller = controller;
		this.data = controller.getData();
		this.frame = frame;

		concreteView(type, null);
		this.frame.setResizable(true);
		this.frame.setVisible(true);
	}

	@Override
	public void concreteView(ViewType type, LocalDate period) {
		AdjustablePanel header;
		if (Objects.requireNonNull(type) == ViewType.TEXTS) {
			table = new ProdTimerTable(frame, new TextTableModel(new TextModel(data)));
			table.setProgress(TextTableModel.Header.TOGO.ordinal());
			header = createHeader(type.getLocale());

			columnController = new ColumnController(table);
			columnController.adjustColumns();

			SettingsManager.loadSettings(type, frame);
		} else {
			table = new ProdTimerTable(frame, new TimeTableModel(new CalendarModel(controller.getData())));
			table.setProgress(TimeTableModel.Header.TOGO.ordinal());
			header = createHeader(type.getLocale());

			columnController = new ColumnController(table);
			columnController.adjustColumns();

			SettingsManager.loadSettings(type, frame);
		}


		frame.setHeader(header);
		frame.setMainComponent(new JScrollPane(table));
		frame.addComponentListener(new SizeStepAdapter(700, 125, header));
	}

	private AdjustablePanel createHeader(String headerText) {
		String finalText = headerText + " : " + getTextNamePresentation();
		JLabel header = new JLabel(finalText);
		header.setHorizontalAlignment(SwingConstants.CENTER);

		JDialog dialog = new CalendarSettings(this, finalText,
				data.getSecond());

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

	public String getTextNamePresentation() {
		List<SelectableText> selectedTexts = data.getSecond().stream().filter(SelectableText::isSelected).toList();

		if (selectedTexts.size() == data.getSecond().size()) {
			return "Все тексты";
		} else {
			return switch (selectedTexts.size()) {
				case 0 -> "Тексты не выбраны";
				case 1 -> selectedTexts.getFirst().getText().getName();
				default -> "Несколько текстов";
			};
		}
	}

	public void pinTable(boolean flag) {
		table.pinElements(flag);
	}

	public void saveColumnPositions() {
		columnController.saveColumnPositions();
	}

	@Override
	public void setTextId(List<SelectableText> textId) {

	}

	@Override
	public List<SelectableText> getSelectableTexts() {
		return data.getSecond();
	}

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

	@Override
	public DBSettings getSettings() {
		return controller.getDbSettings();
	}
}
