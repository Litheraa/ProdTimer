package litheraa.view.calendar;

import lombok.Setter;

import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class DaysPanel extends JPanel {
	private int rows;
	private final int columns;
	private final int hGap;
	private final int vGap;

	public DaysPanel(int rows, int columns, int hGap, int vGap) {
		super(new GridLayout(rows, columns, hGap, vGap));
		this.rows = rows;
		this.columns = columns;
		this.hGap = hGap;
		this.vGap = vGap;
	}

	public Dimension getDaySize(Dimension containerSize) {
		return new Dimension((containerSize.width / columns) - hGap * (Integer.max(columns - 2, 0)),
				(containerSize.height / rows) - vGap * (Integer.max(rows - 2, 0)));
	}

	public void setRows(int rows) {
		this.rows = rows;
		setLayout(new GridLayout(rows, columns, hGap, vGap));
	}

	public boolean isEmpty() {
		return Arrays.stream(getComponents()).noneMatch(comp -> comp instanceof JPanel);
	}

	public void clear() {
		for (Component comp : getComponents()) {
			if (comp instanceof JPanel) {
				remove(comp);
			}
		}
	}
}
