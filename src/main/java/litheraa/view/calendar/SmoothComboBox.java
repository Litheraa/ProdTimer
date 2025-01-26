package litheraa.view.calendar;

import litheraa.ViewController;
import litheraa.view.themes.ThemeColors;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;

public class SmoothComboBox<E> extends JComboBox<E> {
	public SmoothComboBox(ArrayList<E> context, int showItem) {
		//noinspection unchecked
		super((E[]) context.toArray());
		initialize();
		showItem = showItem == - 1 ? context.size() - 1 : showItem;
		setSelectedIndex(showItem);
	}

	public SmoothComboBox(E[] context, int showItem) {
		super(context);
		initialize();
		showItem = showItem == - 1 ? context.length - 1 : showItem;
		setSelectedIndex(showItem);
	}

	private void initialize() {
		for (Component comp : getComponents()) {
			if (comp instanceof AbstractButton) {
				remove(comp);
			}
		}
		setFont(new Font("Georgia", Font.BOLD, 28));
		setBorder(BorderFactory.createEmptyBorder());
		setForeground(((ThemeColors) ViewController.getTheme()).getAccentForeground());
		addMouseListener(new MouseAdapter() {
			@Override
			public void mouseReleased(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON1) {
					if (!isPopupVisible()) {
						showPopup();
					}
				}
			}
		});
		setEnabled(false);
	}
}
