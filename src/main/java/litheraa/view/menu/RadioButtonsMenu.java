package litheraa.view.menu;

import com.github.weisj.darklaf.theme.Theme;
import litheraa.view.themes.OneDark;
import litheraa.view.util.ThemeSupplier;
import lombok.Getter;
import org.jdesktop.swingx.VerticalLayout;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.event.*;
import java.util.ArrayList;

public class RadioButtonsMenu extends JMenu {
	@Getter
	private final int buttonAmount;
	private final ArrayList<JRadioButton> BUTTONS;

	public RadioButtonsMenu(String menuName, String[] buttonsNames, int selectedButton) {
		super(menuName);
		buttonAmount = buttonsNames.length;
		BUTTONS = new ArrayList<>();

		ButtonGroup buttonGroup = new ButtonGroup();
		for (String buttonsName : buttonsNames) {
			JRadioButton button = configureButton(buttonsName);
			BUTTONS.add(button);
			buttonGroup.add(button);
			add(button);
		}
		getPopupMenu().setLayout(new VerticalLayout());
		buttonGroup.setSelected(BUTTONS.get(selectedButton).getModel(), true);

//		TODO найти способ помечать кнопки через попап
		/*getPopupMenu().addMouseMotionListener(new MouseMotionAdapter() {
			@Override
			public void mouseMoved(MouseEvent e) {
				int selectedButtonNo = e.getY() / BUTTONS[0].getHeight();
				BUTTONS[selectedButtonNo].setOpaque(true);
				if (lastMouseYPos < e.getY()) {
					lastMouseYPos = e.getY();
					BUTTONS[selectedButtonNo - 1].setOpaque(false);
				} else {
					lastMouseYPos = e.getY();
					BUTTONS[selectedButtonNo + 1].setOpaque(true);
				}
				BUTTONS[selectedButtonNo].setBackground(((ThemeColors) CalendarController.getTheme()).getSelectionBackground());
			}
		});*/
	}

	private static @NotNull JRadioButton configureButton(String name) {
/// white spase here to make some distance between left edge of button and popup, which is missing in these themes
		JRadioButton button = new JRadioButton(name + " ");
/// One Dark has different selection colors in general and for menu. So, its patch for it
		Theme theme = ThemeSupplier.getTheme();
		if (theme.getName().equals("One Dark")) {
			button.setBackground(((OneDark) ThemeSupplier.getTheme()).getSELECTION_MENU());
		} else {
			button.setBackground(ThemeSupplier.getThemeColor().getSelectionBackground());
		}
		button.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseEntered(MouseEvent e) {
				button.setOpaque(true);
			}

			@Override
			public void mouseExited(MouseEvent e) {
				button.setOpaque(false);
			}
		});
		return button;
	}

	public JRadioButton getButton(int buttonNo) {
		return BUTTONS.get(buttonNo);
	}
}
