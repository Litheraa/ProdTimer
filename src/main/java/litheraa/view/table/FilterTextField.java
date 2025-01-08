package litheraa.view.table;

import lombok.Getter;
import lombok.Setter;

import javax.swing.*;

@Setter
public class FilterTextField extends JTextField {
	@Getter
	private TableSorter sorter;
	private HeaderPopup popup;

	public FilterTextField(boolean isEditable) {
		setColumns(6);
		setFocusable(isEditable);
		setVisible(true);
	}

	public FilterTextField(TableSorter sorter, HeaderPopup popup, boolean isEditable) {
		this(isEditable);
		this.sorter = sorter;
		this.popup = popup;
		setColumns(10);

		addActionListener(e -> {
			setFilter(getText());
			popup.setVisible(false);
		});
	}

	public void setFilter(String value) {
		int column = popup.getColumnNo();
		sorter.setFilter(column, value);
		popup.setFilterStorageElement(value, column);
	}
}
