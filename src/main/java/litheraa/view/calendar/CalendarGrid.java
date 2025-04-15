package litheraa.view.calendar;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.stream.IntStream;

public class CalendarGrid extends JPanel {
	private int addPosition;

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
		int firstDayInt = firstDay.getDayOfWeek().getValue();
		addPosition = firstDayInt - 1;
		IntStream.range(0, (firstDayInt + columns - lastDay.getDayOfWeek().getValue()) - 2)
				.mapToObj(i -> new Filler())
				.forEach(this::addFillers);
	}

	public boolean isEmpty() {
		return Arrays.stream(getComponents()).noneMatch(comp -> comp instanceof JPanel);
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
		super.removeAll();
		addPosition = 0;
	}

	@Override
	public Component add(Component comp) {
		return super.add(comp, addPosition++);
	}

	private void addFillers(Filler filler) {
		super.add(filler);
	}

	private static final class Filler extends JPanel {
		private Filler() {
			setVisible(false);
		}
	}
}
