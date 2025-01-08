package litheraa.view;

import javax.swing.*;

public class RadioButtonsMenu extends JMenu {
	@lombok.Getter
	private final int BUTTONS_AMOUNT;
	private final JRadioButton[] BUTTONS;

	public RadioButtonsMenu(String menuName, int buttonsAmount, String[] buttonsNames, int selectedButton) {
		super(menuName);
		BUTTONS_AMOUNT = buttonsAmount;
		BUTTONS = new JRadioButton[buttonsAmount];

		ButtonGroup buttonGroup = new ButtonGroup();
		for (int i = 0; i < buttonsAmount; i++) {
			BUTTONS[i] = new JRadioButton(buttonsNames[i]);
			buttonGroup.add(BUTTONS[i]);
			add(BUTTONS[i]);
		}
		buttonGroup.setSelected(BUTTONS[selectedButton].getModel(), true);
	}

	public JRadioButton getButton(int buttonNo) {
		return BUTTONS[buttonNo];
	}
}
