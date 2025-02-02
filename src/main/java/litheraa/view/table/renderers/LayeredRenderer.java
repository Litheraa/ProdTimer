package litheraa.view.table.renderers;

import litheraa.data.TextEnum;
import litheraa.util.MeasureUnit;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class LayeredRenderer extends DefaultTableCellRenderer {
	private final JProgressBar progressBar = new JProgressBar();

	public LayeredRenderer() {
		setOpaque(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		progressBar.setStringPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JPanel container = new JPanel();

		OverlayLayout layout = new OverlayLayout(container);
		container.setLayout(layout);

		progressBar.setMaximum(MeasureUnit.toChars(value));
		progressBar.setValue(MeasureUnit.toChars(table.getValueAt(row, table.convertColumnIndexToView(TextEnum.TEXT_CHARS.ordinal())).toString()));
		container.add(progressBar, 0, 0);

		if (value.toString().matches("0")) {
			return super.getTableCellRendererComponent(table, "объем не указан", isSelected, hasFocus, row, column);
		}
		return container;
	}
}
