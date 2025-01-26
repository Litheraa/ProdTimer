package litheraa.view.calendar;

import javax.swing.*;
import java.awt.*;

/// For some strange reason standart JProgressBar(JProgressBar.VERTICAL)
///  while in ProgressContainer filled from top to bottom.
/// There is patch to change it

public class FlippedProgressBar extends JProgressBar {
	public FlippedProgressBar() {
	}

	public FlippedProgressBar(int orientation) {
		super(orientation);
	}

	@Override
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;
		g2d.scale(-1, -1);
		g2d.translate(-getWidth(), -getHeight());
		super.paintComponent(g2d);
	}
}
