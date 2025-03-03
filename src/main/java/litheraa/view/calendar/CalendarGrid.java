package litheraa.view.calendar;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CalendarGrid extends JPanel {
	private int rows;
	private final int columns = 7;
	private final GridLayout layout = new GridLayout();
	private int addPosition;
	private final int firstDay;

	public CalendarGrid(int rows, int hGap, int vGap, int firstDay, int lastDay) {
		setLayout(layout);
		this.rows = rows;
		this.firstDay = firstDay;
		addPosition = firstDay;
		layout.setRows(rows);
		layout.setColumns(columns);
		layout.setHgap(hGap);
		layout.setVgap(vGap);
		IntStream.range(0, (firstDay + (columns - lastDay)) - 2).mapToObj(i -> new Filler()).forEach(this::addFiller);
	}

	public Dimension getDaySize(Dimension containerSize) {
		return new Dimension(containerSize.width / columns - columns,
				containerSize.height / rows - rows);
	}

	public void setRows(int rows) {
		this.rows = rows;
		layout.setRows(rows);
	}

	public boolean isEmpty() {
		return Arrays.stream(getComponents()).noneMatch(comp -> comp instanceof JPanel);
	}

	public List<Component> getComps() {
		List<Component> components = new ArrayList<>();
		for (int i = addPosition - 1; i >= firstDay; i--) {;
			components.add(getComponent(i));
		}
		return components.reversed();
	}

	@Override
	public Component add(String name, Component comp) {
		addImpl(comp, name, addPosition++);
		return comp;
	}

	@Override
	public void remove(Component comp) {
		if (!(comp instanceof Filler)) {
			--addPosition;
			super.remove(comp);
		}
	}

	@Override
	public void removeAll() {
		addPosition = 0;
		super.removeAll();
	}

	private void addFiller(Filler filler) {
		super.add(filler);
	}

	@Override
	public Component add(Component comp) {
			return super.add(comp, addPosition++);
	}

	private static final class Filler extends JPanel {
		private Filler(){
			setVisible(false);
		}
	}
}
