package litheraa.view.table.renderers;

import lombok.Setter;
import org.jdesktop.swingx.HorizontalLayout;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.util.Objects;

@Setter
public class HeaderRenderer implements TableCellRenderer {
	private boolean isFiltered;
	private SortOrder sortOrder;

	ImageIcon angleUp = new ImageIcon(Objects.requireNonNull(HeaderRenderer.class.getClassLoader().getResource("angle-small-up.png")));
	ImageIcon angleDown = new ImageIcon(Objects.requireNonNull(HeaderRenderer.class.getClassLoader().getResource("angle-small-down.png")));
	ImageIcon filter = new ImageIcon(Objects.requireNonNull(HeaderRenderer.class.getClassLoader().getResource("filterImage.png")));

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		//All images are part of <uicons> icons pack, downloaded from www.flaticon.com
		JPanel panel = new JPanel(new HorizontalLayout(5));
		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.RAISED));
		JLabel header = new JLabel(table.getColumnName(column), SwingUtilities.CENTER);
		JLabel filterIcon = new JLabel();
		JLabel sortIcon = new JLabel();
		switch (sortOrder) {
			case ASCENDING -> sortIcon.setIcon(angleDown);
			case DESCENDING -> sortIcon.setIcon(angleUp);
			case UNSORTED -> sortIcon.setIcon(null);
			case null -> sortIcon.setIcon(null);
		}
		if (!isFiltered) {
			filterIcon.setIcon(null);
		} else {
			filterIcon.setIcon(filter);
		}
		panel.add(header);
		panel.add(filterIcon);
		panel.add(sortIcon);
		return panel;
	}
}
