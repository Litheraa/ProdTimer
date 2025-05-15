package litheraa.view.selection_table;

import litheraa.data.entities.SelectableText;

import javax.swing.*;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableColumn;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

class TextSelectionTable extends JTable {
	private final TextSelectionTableModel model;

	TextSelectionTable(ArrayList<SelectableText> texts) {
		model = new TextSelectionTableModel(texts);
		setModel(model);
		setRowHeight(30);

		JCheckBox headerCheckBox = new JCheckBox("Выбрать все", texts.stream().allMatch(SelectableText::isSelected));

		JTableHeader header = getTableHeader();
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				boolean b = !headerCheckBox.isSelected();
				model.setAllSelected(b);
				headerCheckBox.setSelected(b);
			}
		});

		TableColumn column = getColumnModel().getColumn(0);
		column.setCellRenderer(new CheckBoxCellRenderer());
		column.setCellEditor(new CheckBoxCellEditor());
		column.setHeaderRenderer((table, value, isSelected, hasFocus, row, column1) -> headerCheckBox);

		setPreferredScrollableViewportSize(new Dimension(getWidth(), (getRowCount() + 1) * 30));
	}

	List<SelectableText> getSelectableTexts() {
		return model.getSelectableTexts();
	}


}
