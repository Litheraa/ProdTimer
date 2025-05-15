package litheraa.view.menu;

import org.jdesktop.swingx.color.ColorUtil;

import javax.swing.*;
import java.awt.*;

public class ColorfulMenuItem extends JMenuItem {

	public ColorfulMenuItem(String text) {
		super("       " + text);
	}

	@Override
	protected void paintComponent(Graphics g) {
		if (getModel().isArmed() || getModel().isRollover()) {
			g.setColor(UIManager.getColor("Menu.selectionBackground"));
		} else {
			g.setColor(ColorUtil.setAlpha(UIManager.getColor("Menu.background"), 0));
		}
		g.fillRect(0, 0, getWidth(), getHeight());

		if (getModel().isArmed() || getModel().isRollover()) {
			g.setColor(UIManager.getColor("Menu.selectionForeground"));
		} else {
			g.setColor(UIManager.getColor("Menu.foreground"));
		}

		FontMetrics fm = g.getFontMetrics();
		int textX = getInsets().left;
		int textY = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
		g.drawString(getText(), textX, textY);
	}
}

