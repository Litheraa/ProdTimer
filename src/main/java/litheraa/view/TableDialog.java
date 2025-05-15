package litheraa.view;

import litheraa.controller.ProdTimerController;
import litheraa.controller.SettingsController;
import litheraa.settings.DBSettings;
import litheraa.util.CalendarWrapper;
import litheraa.view.util.IntegerFilter;
import lombok.SneakyThrows;
import org.jdesktop.swingx.JXDialog;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;
import java.util.function.BiConsumer;

public class TableDialog extends JDialog {
	private final Container container = getContentPane();

	public TableDialog(JComponent component, Point location) {
//		super(component);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
		setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		setLocation(location);
		setAlwaysOnTop(true);
		setBackground(Color.YELLOW);
	}

	public void createDayChooserDialog(String title, String dialogString, SettingsController controller, BiConsumer<SettingsController, String> biConsumer) {
		setTitle(title);
		setSize(477, 267);

		DateChooseDialog dialog = new DateChooseDialog();
		dialog.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setText(CalendarWrapper.wrapToString(dialog.getSelectionDate()));
			}
		});
		dialog.wrapWithButtons(this, dialogString);
		add(dialog);

		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				biConsumer.accept(controller, dialog.getText());
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				if (SwingUtilities.isDescendingFrom(e.getOppositeWindow(), TableDialog.this)) {
					return;
				}
				biConsumer.accept(controller, dialog.getText());
				TableDialog.this.setVisible(false);
			}
		});
		setVisible(true);
	}

	public void createProdVolumeDialog() {
		setTitle("Знаков в день");
		JTextField field = new JTextField(String.valueOf(SettingsController.getProdGoal()));

		Action action = new AbstractAction() {
			@SneakyThrows
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsController.setProdVolume(field.getText());
				setVisible(false);
			}
		};

		addIntegerFilterField(action, field);
//		addSaveButton(action);
//		setSingleFrame();
		pack();
		setVisible(true);
	}

	public void createUpdateIntervalDialog(ProdTimerController controller) {
		DBSettings settings = controller.getDbSettings();;
		setTitle("Обновлять данные каждые (минут)");
		JTextField field = new JTextField(String.valueOf(settings.getUpdateInterval()));

		Action action = new AbstractAction() {
			@SneakyThrows
			@Override
			public void actionPerformed(ActionEvent e) {
				String interval = field.getText();
				if (Integer.parseInt(interval) < 5 || Integer.parseInt(interval) >= 1000) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(TableDialog.this, "Не меньше 5 минут");
				}
				settings.setUpdateInterval(Short.parseShort(field.getText()));
				setVisible(false);
			}
		};

		addIntegerFilterField(action, field);
		addSaveButton(action);
		setSingleFrame();
		pack();
		setVisible(true);
	}

	public void createProdNameLengthDialog(ProdTimerController controller) {
		DBSettings settings = controller.getDbSettings();
		setTitle("Знаков в названии проды");
		JTextField field = new JTextField(String.valueOf(settings.getLength()));

		Action action = new AbstractAction() {
			@SneakyThrows
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!field.getText().isEmpty() && Integer.parseInt(field.getText()) > 50) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(TableDialog.this, "Не больше 50 знаков");
				} else {
					settings.setLength(Short.parseShort(field.getText()));
					setVisible(false);
				}
			}
		};

		addIntegerFilterField(action, field);
		addSaveButton(action);
		setSingleFrame();
		pack();
		setVisible(true);
	}

	private void setSingleFrame() {
		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				if (SwingUtilities.isDescendingFrom(e.getOppositeWindow(), TableDialog.this)) {
					return;
				}
				TableDialog.this.setVisible(false);
			}
		});
	}

	private void addSaveButton(Action action) {
		JButton button = new JButton(action);
		button.setText("Сохранить");
		button.setAlignmentX(Component.CENTER_ALIGNMENT);
		container.add(button);
	}

	private void addIntegerFilterField(Action action, JTextField field) {
		AbstractDocument document = (AbstractDocument) field.getDocument();
		document.setDocumentFilter(new IntegerFilter());
		field.setAction(action);
		container.add(field);
	}
}
