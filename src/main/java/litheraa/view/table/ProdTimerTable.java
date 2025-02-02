package litheraa.view.table;

import litheraa.data.ColumnDataTypeEnum;
import litheraa.data.RoutineEnum;
import litheraa.view.MainFrame;
import litheraa.data.TextEnum;
import litheraa.view.table.renderers.HeaderRenderer;
import litheraa.view.table.renderers.LayeredRenderer;
import litheraa.view.table.renderers.ProgressRenderer;
import litheraa.view.table.renderers.TextAreaRenderer;
import litheraa.view.util.DoubleFilter;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class ProdTimerTable extends JTable {
	@Setter
	private String[] filterStorage;
	@Getter
	private final MainFrame mainFrame;
	@Getter
	private TableSorter sorter;
	@Getter
	private int activeColumn;
	private final RoutineModel model;

	public ProdTimerTable(MainFrame mainFrame, RoutineModel model) {
		this.mainFrame = mainFrame;
		this.model = model;
		setModel(model);
		sorter = new TableSorter(model, this);

		int columns = getColumnModel().getColumnCount();
		filterStorage = new String[columns];
		for (int i = 0; i < columns; i++) {
			filterStorage[i] = "";
			TableColumn column = getColumnModel().getColumn(i);
			setDoubleFilterToColumn(column);
			column.setHeaderRenderer(new HeaderRenderer());
		}
		setRowSorter(sorter);

		HeaderPopup popup = new HeaderPopup(this);

		JTableHeader header = getTableHeader();
		header.setComponentPopupMenu(popup);
		header.addMouseListener(new MouseAdapter() {
			@Override
			public void mousePressed(MouseEvent e) {
				if (e.getButton() == MouseEvent.BUTTON3) {
					activeColumn = header.columnAtPoint(e.getPoint());
				} else if (e.getButton() == MouseEvent.BUTTON1) {
					activeColumn = header.columnAtPoint(e.getPoint());
					((HeaderRenderer) getColumn(activeColumn).getHeaderRenderer()).setSortOrder(sorter.nextSortOrder());
					getMainFrame().repaint();
				}
			}
		});
	}

	public void setTextArea() {
		getColumnModel().getColumn(convertColumnIndexToView(RoutineEnum.NAMES.ordinal())).setCellRenderer(new TextAreaRenderer());
	}

	public void setProgressBar() {
		getColumnModel().getColumn(convertColumnIndexToView(RoutineEnum.PROD_CHARS.ordinal())).setCellRenderer(new ProgressRenderer());
	}

	public void setProgress() {
		getColumnModel().getColumn(convertColumnIndexToView(TextEnum.CHARS_TOTAL.ordinal())).setCellRenderer(new LayeredRenderer());
	}

	@Override
	public TableColumn getColumn(@NotNull Object identifier) {
		return getColumnModel().getColumn((Integer) identifier);
	}

	public ColumnDataTypeEnum getColumnType(int column) {
		return model.getColumnType(convertColumnIndexToModel(column));
	}

	public void pinElements(boolean isTablePinned) {
		getTableHeader().setReorderingAllowed(!isTablePinned);
		getTableHeader().setResizingAllowed(!isTablePinned);
	}

	private void setDoubleFilterToColumn(TableColumn column) {
		if (column.getModelIndex() == TextEnum.CHARS_TOTAL.ordinal()) {
			JTextField charsTotalValue = new JTextField();
			AbstractDocument document = (AbstractDocument) charsTotalValue.getDocument();
			document.setDocumentFilter(new DoubleFilter());
			column.setCellEditor(new DefaultCellEditor(charsTotalValue));
		}
	}

	public void setFilterStorage(String value, int storageNo) {
		filterStorage[storageNo] = value;
	}

	public String getFilterStorage(int storageNo) {
		return filterStorage[storageNo];
	}

	public void setFilter(boolean filtered) {
		((HeaderRenderer) getColumn(activeColumn).getHeaderRenderer()).setFiltered(filtered);
		getMainFrame().repaint();
	}
}