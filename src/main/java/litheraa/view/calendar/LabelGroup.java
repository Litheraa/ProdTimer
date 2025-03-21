package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.IntegerFilter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.ConstraintFactory;
import litheraa.view.util.fabric.FontLightWeight;
import litheraa.view.util.fabric.IconFactory;
import org.jdesktop.swingx.VerticalLayout;

import javax.swing.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.*;

public class LabelGroup extends JPanel implements AdjustableComponentInterface {
	private final JLabel goalLabel;
	private final JLabel writtenLabel;
	private final JLabel toGoLabel;
	private final ConstraintFactory CONSTRAINT_FABRIC;
	private final FontLightWeight FONT_FABRIC;
	private final IconFactory ICON_FABRIC;
	private Container PARENT = getParent();
	private static final String[] ICON_NAMES = {"mission.png", "magic-book.png", "writed-book.png"};

	public LabelGroup(int written, int goal, ConstraintFactory constraintFactory, FontLightWeight fontLightWeight, IconFactory iconFactory) {
		CONSTRAINT_FABRIC = constraintFactory;
		FONT_FABRIC = fontLightWeight;
		ICON_FABRIC = iconFactory;
		goalLabel = new JLabel(String.valueOf(goal));
		writtenLabel = new JLabel(String.valueOf(written));
		toGoLabel = new JLabel(String.valueOf((goal - written)));

		add(goalLabel);
		add(writtenLabel);
		add(toGoLabel);

		setGoalPopUp();
		setLayout(new VerticalLayout(2));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
	}

	private void setGoalPopUp() {
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
//					controller.setGoal(Integer.parseInt(goalString), timeId);
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
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		PARENT.add(this, CONSTRAINT_FABRIC.getConstraints(getUIClassID(), ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		for (int i = 0; i < getComponentCount(); i++) {
			JLabel label = (JLabel) getComponent(i);
			label.setFont(FONT_FABRIC.getFont(getUIClassID(), step, 4, Font.BOLD));
			label.setIcon(ICON_FABRIC.getIcon(ICON_NAMES[i], step, 4));
		}
	}

	@Override
	public void wireWithParent(JComponent parent) {
		PARENT = parent;
		PARENT.add(this);
	}
}
