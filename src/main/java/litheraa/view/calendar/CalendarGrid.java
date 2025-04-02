package litheraa.view.calendar;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;

public class CalendarGrid extends JPanel {
	private int addPosition;
	private final int firstDay;

	public CalendarGrid(int rows, int hGap, int vGap, LocalDate firstDay, LocalDate lastDay) {
		int columns = 7;

		GridLayout layout = new GridLayout();
		layout.setRows(rows);
		layout.setColumns(columns);
		layout.setHgap(hGap);
		layout.setVgap(vGap);

		setLayout(layout);

		if (!isEmpty()) {
			removeAll();
		}

///     Fills CalendarGrid with empty JPanels to add empty spase before first and after last day of month
		this.firstDay = firstDay.getDayOfWeek().getValue();
		addPosition = this.firstDay - 1;
		IntStream.range(0, (this.firstDay + columns - lastDay.getDayOfWeek().getValue()) - 2).mapToObj(i -> new Filler()).forEach(this::addFillers);
	}

	public boolean isEmpty() {
		return Arrays.stream(getComponents()).noneMatch(comp -> comp instanceof JPanel);
	}

	public List<Component> getComps() {
		List<Component> components = new ArrayList<>();
		for (int i = addPosition - 1; i >= firstDay; i--) {
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
	private void addFillers(Filler filler) {
		super.add(filler);
	}

	private static final class Filler extends JPanel {
		private Filler() {
			setVisible(false);
		}
	}
}
