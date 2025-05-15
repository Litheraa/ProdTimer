package litheraa.view.table.renderers;

import javax.swing.table.DefaultTableCellRenderer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class LocalDateRenderer extends DefaultTableCellRenderer {
	private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

	@Override
	protected void setValue(Object value) {
		if (value instanceof LocalDate) {
			setText(((LocalDate) value).format(formatter));
		} else {
			super.setValue(value);
		}
	}
}
