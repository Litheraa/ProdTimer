package litheraa.view.menu;

import lombok.Getter;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.Iterator;

public class RadioButtonsMenu extends JMenu {
	@Getter
	private final int buttonAmount;
	ButtonGroup buttonGroup;

	public RadioButtonsMenu(String menuName, String[] buttonsNames, int selectedButton) {
		super(menuName);
		buttonAmount = buttonsNames.length;

		buttonGroup = new ButtonGroup();

		for (String buttonsName : buttonsNames) {
			JRadioButton button = configureButton(buttonsName);
			buttonGroup.add(button);
			add(button);
		}
		getPopupMenu().setLayout(new VerticalLayout());

		JRadioButton button = getButton(selectedButton);
		button.setBackground(UIManager.getColor("Menu.selectionBackground"));
		button.setForeground(UIManager.getColor("Menu.selectionForeground"));
		buttonGroup.setSelected(button.getModel(), true);
	}

	private static @NotNull JRadioButton configureButton(String name) {
/// white spase here to make some distance between left edge of button and popup, which is missing in these themes
		JRadioButton button = new JRadioButton(name + " ");
			Color b = button.getBackground();
			Color f = button.getForeground();
			button.addMouseListener(new MouseAdapter() {
				@Override
				public void mouseEntered(MouseEvent e) {
					button.setBackground(UIManager.getColor("Menu.selectionBackground"));
					button.setForeground(UIManager.getColor("Menu.selectionForeground"));
				}

				@Override
				public void mouseExited(MouseEvent e) {
					button.setBackground(b);
					button.setForeground(f);
					if (button.isSelected()) {
						button.setBackground(UIManager.getColor("Menu.selectionBackground"));
						button.setForeground(UIManager.getColor("Menu.selectionForeground"));
					}
				}
			});
		return button;
	}

	public JRadioButton getButton(int buttonNo) {
		Iterator<AbstractButton> iterator = buttonGroup.getElements().asIterator();
		for (int i = 0; i != buttonNo; i++) {
			iterator.next();
		}
		return (JRadioButton) iterator.next();
	}
}
