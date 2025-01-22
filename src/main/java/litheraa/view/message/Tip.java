package litheraa.view.message;

import litheraa.SettingsController;
import litheraa.view.MainFrame;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Tip extends JDialog {
	private static int tipNo = 0;
	private static final int TIP_COUNT = MessageUtil.getTipCount();
	private static Tip tipDialog;
	private static JXLabel tipText;
	private static JLabel tipCounter;
	private static JLabel tipIcon;

	private Tip(MainFrame mainFrame) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				tipDialog.setVisible(false);
			}
		});
		setResizable(false);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setSize(500, 250);
		setLocationRelativeTo(mainFrame);

		JCheckBox checkBox = new JCheckBox("больше не показывать");
		checkBox.addActionListener(e -> {
			SettingsController.switchTipShow();
			checkBox.setSelected(SettingsController.isTipsShow());
		});

		JButton ok = new JButton("Ok");
		ok.addActionListener(e -> setVisible(false));

		JButton previous = new JButton("предыдущая");
		previous.addActionListener(e -> previousTip(mainFrame));

		JButton next = new JButton("следующая");
		next.addActionListener(e -> nextTip(mainFrame));

		tipText = new JXLabel();

		tipText.setHorizontalTextPosition(SwingConstants.RIGHT);
		tipText.setTextAlignment(JXLabel.TextAlignment.CENTER);

		tipIcon = new JLabel();

		tipCounter = new JLabel();

///		Do not change this lines relative position. Otherwise, it will screw all messages numeration
		{
			tipText.setText(MessageUtil.getTip(tipNo).getFirst());
			tipIcon.setIcon(MessageUtil.getTip(tipNo).getSecond());
			tipCounter.setText((tipNo + 1)  + "/(" + TIP_COUNT + ")");
		}

		SpringLayout layout = new SpringLayout();

		Container container = getContentPane();
		container.setLayout(layout);

		layout.putConstraint(SpringLayout.NORTH, tipText, 10, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, tipText, 5, SpringLayout.EAST, tipIcon);
		layout.putConstraint(SpringLayout.EAST, tipText, 0, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.SOUTH, tipText, -5, SpringLayout.NORTH, ok);
		layout.putConstraint(SpringLayout.NORTH, tipIcon, 0, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.WEST, tipIcon, 0, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.SOUTH, tipIcon, -5, SpringLayout.NORTH, ok);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ok, 0, SpringLayout.HORIZONTAL_CENTER, container);
		layout.putConstraint(SpringLayout.EAST, previous, -10, SpringLayout.WEST, ok);
		layout.putConstraint(SpringLayout.WEST, next, 10, SpringLayout.EAST, ok);
		layout.putConstraint(SpringLayout.SOUTH, checkBox, 0, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.SOUTH, ok, -5, SpringLayout.NORTH, checkBox);
		layout.putConstraint(SpringLayout.SOUTH, previous, -5, SpringLayout.NORTH, checkBox);
		layout.putConstraint(SpringLayout.SOUTH, next, -5, SpringLayout.NORTH, checkBox);
		layout.putConstraint(SpringLayout.SOUTH, tipCounter, 0, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.WEST, checkBox, 0, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.EAST, tipCounter, 0, SpringLayout.EAST, container);

		container.add(previous);
		container.add(ok);
		container.add(next);
		container.add(checkBox);
		container.add(tipCounter);
		container.add(tipText);
		container.add(tipIcon);

		tipText.setLineWrap(true);

		setVisible(true);
	}

	public static void forceShowTip(MainFrame mainFrame) {
		if (tipDialog == null) {
			tipDialog = new Tip(mainFrame);
		} else {
			doNewTip();
		}
	}

	private void nextTip(MainFrame mainFrame) {
		tipNo = ++tipNo % TIP_COUNT;
		forceShowTip(mainFrame);
	}

	private void previousTip(MainFrame mainFrame) {
		tipNo = (--tipNo + TIP_COUNT) % TIP_COUNT;
		forceShowTip(mainFrame);
	}

	private static void doNewTip() {
		tipText.setText(MessageUtil.getTip(tipNo).getFirst());
		tipIcon.setIcon(MessageUtil.getTip(tipNo).getSecond());
		tipCounter.setText(tipNo + 1 + "/(" + TIP_COUNT + ")");
		tipDialog.setVisible(true);
	}
}
