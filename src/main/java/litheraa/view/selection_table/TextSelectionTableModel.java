package litheraa.view.selection_table;

import litheraa.data.entities.SelectableText;

import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.List;

class TextSelectionTableModel extends AbstractTableModel {
	private final ArrayList<SelectableText> texts;

	public TextSelectionTableModel(ArrayList<SelectableText> textList) {
		texts = textList;
	}

	@Override
	public int getRowCount() {
		return texts.size();
	}

	@Override
	public int getColumnCount() {
		return 1;
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		return texts.get(rowIndex);
	}

	@Override
	public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
		if (aValue instanceof Boolean) {
			texts.get(rowIndex).setSelected((Boolean) aValue);
			fireTableRowsUpdated(rowIndex, rowIndex);
		}
	}

	@Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
		return true;
	}

	@Override
	public String getColumnName(int column) {
		return SelectableText.getTextNamePresentation(texts);
	}

	void setAllSelected(boolean selected) {
		for (SelectableText text : texts) {
			text.setSelected(selected);
		}
		fireTableDataChanged();
	}

	List<SelectableText> getSelectableTexts() {
		return texts;
	}

}
