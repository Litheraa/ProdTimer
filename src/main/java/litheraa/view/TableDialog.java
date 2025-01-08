package litheraa.view;

import litheraa.SettingsController;
import litheraa.util.CalendarWrapper;
import litheraa.view.util.IntegerFilter;
import lombok.SneakyThrows;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;

public class TableDialog extends JDialog {
	private final Container container = getContentPane();

	public TableDialog(Point location) {
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setResizable(false);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
		setLayout(new BoxLayout(container, BoxLayout.Y_AXIS));
		setLocation(location);
	}

	public void createDayChooserDialog() {
		setTitle("Не показывать тексты до");
		setSize(477, 267);

		DateChooseDialog dialog = new DateChooseDialog();
		dialog.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dialog.setText(CalendarWrapper.wrapToString(dialog.getSelectionDate()));
			}
		});
		dialog.wrapWithButtons(this, SettingsController.getCutDate());
		add(dialog);

		addWindowFocusListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				SettingsController.setCutDate(dialog.getText());
			}

			@Override
			public void windowLostFocus(WindowEvent e) {
				if (SwingUtilities.isDescendingFrom(e.getOppositeWindow(), TableDialog.this)) {
					return;
				}
				SettingsController.setCutDate(dialog.getText());
				TableDialog.this.setVisible(false);
			}
		});
		setVisible(true);
	}

	public void createProdVolumeDialog() {
		setTitle("Знаков в день");
		JTextField field = new JTextField(String.valueOf(SettingsController.getProdVolume()));

		Action action = new AbstractAction() {
			@SneakyThrows
			@Override
			public void actionPerformed(ActionEvent e) {
				SettingsController.setProdVolume(field.getText());
				setVisible(false);
			}
		};

		addIntegerFilterField(action, field);
		addSaveButton(action);
		setSingleFrame();
		pack();
		setVisible(true);
	}

	public void createUpdateIntervalDialog() {
		setTitle("Обновлять данные каждые (минут)");
		JTextField field = new JTextField(String.valueOf(SettingsController.getUpdateInterval()));

		Action action = new AbstractAction() {
			@SneakyThrows
			@Override
			public void actionPerformed(ActionEvent e) {
				String interval = field.getText();
				if (Integer.parseInt(interval) < 5 || Integer.parseInt(interval) >= 1000) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(TableDialog.this, "Не меньше 5 минут");
				}
				SettingsController.setUpdateInterval(field.getText());
				setVisible(false);
			}
		};

		addIntegerFilterField(action, field);
		addSaveButton(action);
		setSingleFrame();
		pack();
		setVisible(true);
	}

	public void createProdNameLengthDialog() {
		setTitle("Знаков в названии проды");
		JTextField field = new JTextField(String.valueOf(SettingsController.getProdNameLength()));

		Action action = new AbstractAction() {
			@SneakyThrows
			@Override
			public void actionPerformed(ActionEvent e) {
				if (!field.getText().isEmpty() && Integer.parseInt(field.getText()) > 50) {
					Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(TableDialog.this, "Не больше 50 знаков");
				} else {
					SettingsController.setProdNameLength(field.getText());
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
