package litheraa.view.table;

import litheraa.view.MainFrame;
import litheraa.view.table.renderers.HeaderRenderer;
import litheraa.view.table.renderers.LayeredRenderer;
import litheraa.view.table.renderers.LocalDateRenderer;
import litheraa.view.util.DoubleFilter;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.*;
import javax.swing.text.AbstractDocument;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.stream.IntStream;

public class ProdTimerTable extends JTable {
	@Setter
	private String[] filterStorage;
	@Getter
	private final MainFrame mainFrame;
	@Getter
	private TableSorter sorter;
	@Getter
	private int activeColumn;

	public ProdTimerTable(MainFrame mainFrame, TimeTableModel model) {
		this.mainFrame = mainFrame;
		this.
		setModel(model);
		setFocusable(false);
		sorter = new TableSorter(model, this);
		sorter.setComparator(1, Comparator.naturalOrder());

		int columns = getColumnModel().getColumnCount();
		filterStorage = new String[columns];
		IntStream.range(0, columns).forEach(i -> {
			filterStorage[i] = "";
			TableColumn column = getColumnModel().getColumn(i);
			setDoubleFilterToColumn(column);
			column.setHeaderRenderer(new HeaderRenderer());
		});
		applyLocalDateRenderers();
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

	private void applyLocalDateRenderers() {
		TableModel model = getModel();
		IntStream.range(0, model.getColumnCount())
				.filter(i -> model.getColumnClass(i).equals(LocalDate.class))
				.forEach(i -> getColumnModel().getColumn(i).setCellRenderer(new LocalDateRenderer()));
	}


	public void setProgress(int columnOrdinal) {
		getColumnModel().getColumn(convertColumnIndexToView(columnOrdinal)).setCellRenderer(new LayeredRenderer());
	}

	@Override
	public TableColumn getColumn(@NotNull Object identifier) {
		return getColumnModel().getColumn((Integer) identifier);
	}

	public void pinElements(boolean isTablePinned) {
		getTableHeader().setReorderingAllowed(!isTablePinned);
		getTableHeader().setResizingAllowed(!isTablePinned);
	}

	private void setDoubleFilterToColumn(TableColumn column) {
		if (column.getModelIndex() == TextTableModel.Header.GOAL.ordinal()) {
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