package litheraa.view.selection_table;

import litheraa.data.entities.SelectableText;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

class CheckBoxCellRenderer extends JCheckBox implements TableCellRenderer {

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		SelectableText text = (SelectableText) value;

		setSelected(text.isSelected());
		setText(text.getText().getName());

		return this;
	}
}
