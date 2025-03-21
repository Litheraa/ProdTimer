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

///     To improve performance, I am emulating the componentResized event of the DayPanel component.
///     The InnerComponentSize interface provides the getComponentSize method used in the AspectRatioAdapter.
///     This way, the AspectRatioAdapter could use the size of one of the DayPanels
///     instead of the size of the original event source.

		IntStream.range(0, (firstDay + (columns - lastDay)) - 2).mapToObj(i -> new Filler()).forEach(this::addFiller);
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

	@Override
	public Component add(Component comp) {
		return super.add(comp, addPosition++);
	}

	/// GritLayout in which days located ignores number of columns (days in week) if rows are set. ///
	/// To prevent weeks with 6 days im forced to add some empty days in the end of the month      ///
	private void addFiller(Filler filler) {
		super.add(filler);
	}

	public Dimension getDayPanelSize() {
		return new Dimension(this.getWidth() / columns - columns,
				this.getHeight() / rows - rows);
	}

	private static final class Filler extends JPanel {
		private Filler(){
			setVisible(false);
		}
	}
}
