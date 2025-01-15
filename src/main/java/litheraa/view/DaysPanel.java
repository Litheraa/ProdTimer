package litheraa.view;

import javax.swing.*;
import java.awt.*;

public class DaysPanel extends JPanel {
	private final int rows;
	private final int columns;
	private final int hgap;
	private final int vgap;

	public DaysPanel(int rows, int columns, int hgap, int vgap) {
		super(new GridLayout(rows, columns, hgap, vgap));
		this.rows = rows;
		this.columns = columns;
		this.hgap = hgap;
		this.vgap = vgap;
	}

	public Dimension getDaySize() {
		return new Dimension((getWidth() / columns) - hgap * (Integer.max(columns - 2, 0)),
				(getHeight() / rows) - vgap * (Integer.max(rows - 2, 0)));
	}
}
