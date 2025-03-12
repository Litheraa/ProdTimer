package litheraa.view.calendar;

import litheraa.view.util.ComponentAdjuster;
import litheraa.view.util.IntegerFilter;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;

public class LabelGroup extends JPanel implements ComponentAdjuster {
	private final JLabel goalLabel;
	private final JLabel valueLabel;
	private final JLabel toGoLabel;
	private final Long timeId;
	private final DayPanelController controller;

	public LabelGroup(DayPanelController controller, int written, int goal, Long timeId) {
		this.controller = controller;
		this.timeId = timeId;
		goalLabel = new JLabel(String.valueOf(goal));
		valueLabel = new JLabel(String.valueOf(written));
		toGoLabel = new JLabel(String.valueOf((goal - written)));

		add(goalLabel);
		add(valueLabel);
		add(toGoLabel);

		setGoalPopUp(controller);
		setLayout(new VerticalLayout(2));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}

	private void setGoalPopUp(DayPanelController controller) {
		JPopupMenu menu = new JPopupMenu();
		JTextField textField = new JTextField(goalLabel.getText());
		textField.setPreferredSize(new Dimension(50, textField.getHeight() + 25));
		AbstractDocument document = (AbstractDocument) textField.getDocument();
		document.setDocumentFilter(new IntegerFilter());
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					String goalString = textField.getText();
					goalLabel.setText(goalString);
					controller.setGoal(Integer.parseInt(goalString), timeId);
//					TODO установка цели в модели времени
					menu.setVisible(false);
				}
			}
		});
		menu.add(textField);
		goalLabel.setComponentPopupMenu(menu);
		goalLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if (!menu.isVisible()) {
					menu.setVisible(true);
					menu.show(goalLabel, goalLabel.getX() + goalLabel.getWidth(), goalLabel.getY()/* - (goalLabel.getHeight() * 4)*/);
					textField.getCaret().setDot(0);
				}
			}
		});
	}

	@Override
	public void adjust(Font font) {
		Icon[] icons = controller.getIcon();
		for (int i = 0; i < getComponents().length; i++) {
			getComponent(i).setFont(font);
			((JLabel) getComponent(i)).setIcon(icons[i]);
		}
	}
}
