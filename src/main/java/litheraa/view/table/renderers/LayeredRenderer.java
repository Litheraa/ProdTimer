package litheraa.view.table.renderers;

import litheraa.view.calendar.ProgressBar;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class LayeredRenderer extends DefaultTableCellRenderer {

	public LayeredRenderer() {
		setOpaque(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		JPanel container = new JPanel();

		OverlayLayout layout = new OverlayLayout(container);
		container.setLayout(layout);

		JProgressBar progressBar = new ProgressBar(Integer.parseInt(value.toString()), 40000);
		container.add(progressBar, 0, 0);

		if (value.toString().matches("0")) {
			return super.getTableCellRendererComponent(table, "объем не указан", isSelected, hasFocus, row, column);
		}
		return container;
	}
}
