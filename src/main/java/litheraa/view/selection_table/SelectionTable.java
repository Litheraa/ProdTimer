package litheraa.view.selection_table;

import litheraa.data.entities.SelectableText;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class SelectionTable extends JScrollPane {
	private final TextSelectionTable table;

	public SelectionTable(ArrayList<SelectableText> texts) {
		table = new TextSelectionTable(texts);
		setViewportView(table);
		setPreferredSize(table.getPreferredScrollableViewportSize());
	}

	public List<SelectableText> getSelectableTexts() {
		return table.getSelectableTexts();
	}
}
