package litheraa.view.selection_table;

import litheraa.data.entities.SelectableText;

import javax.swing.*;
import javax.swing.event.CellEditorListener;
import javax.swing.table.TableCellEditor;
import java.awt.*;

class CheckBoxCellEditor extends AbstractCellEditor implements TableCellEditor {
	private final JCheckBox checkBox;
	private SelectableText text;

	public CheckBoxCellEditor () {
		checkBox = new JCheckBox();
		checkBox.addActionListener(e -> {
			if (text != null) {
				text.setSelected(checkBox.isSelected());
				fireEditingStopped();
			}
		});
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
		text = (SelectableText) value;
		checkBox.setSelected(text.isSelected());
		checkBox.setText(text.getText().getName());

		return checkBox;
	}

	@Override
	public Object getCellEditorValue() {
		return text.isSelected();
	}
}
