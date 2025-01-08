package litheraa.view;

import litheraa.SettingsController;
import litheraa.util.KeysRecord;
import litheraa.view.table.ProdTimerTable;
import litheraa.view.util.ColumnKeysRecord;
import litheraa.util.SettingsUtil;
import org.apache.commons.math3.util.Pair;

import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.IntStream;

public class ColumnController extends SettingsUtil{

	private final MainFrame mainFrame;
	private final Map<Integer, Pair<TableColumn, Integer>> hidedColumns = new HashMap<>();
	private final static ColumnKeysRecord COLUMN_KEYS = new ColumnKeysRecord();

	public ColumnController(MainFrame mainFrame) {
		this.mainFrame = mainFrame;
	}

	private TableColumnModel getColumnModel() {
		return getTable().getColumnModel();
	}

	private ProdTimerTable getTable() {
		return mainFrame.getTable();
	}

	private int getColumnPositionInView(int columnIdentifier) {
		return getTable().convertColumnIndexToView(columnIdentifier);
	}

	private void showColumn(int columnIdentifier) {
		getColumnModel().addColumn(hidedColumns.get(columnIdentifier).getFirst());
		getTable().moveColumn(getColumnPositionInView(columnIdentifier),
				hidedColumns.get(columnIdentifier).getSecond());
		switch (getTableType()) {
			case 0 ->
					SettingsUtil.set(COLUMN_KEYS.getTextKey(columnIdentifier).split("/")[0], String.valueOf(getColumnPositionInView(columnIdentifier)));
			case 1 ->
					SettingsUtil.set(COLUMN_KEYS.getRoutineKey(columnIdentifier).split("/")[0], String.valueOf(getColumnPositionInView(columnIdentifier)));
		}
	}

	private void hideColumn(int columnIdentifier) {
		TableColumn column = getTable().getColumn(columnIdentifier);
		hidedColumns.put(columnIdentifier, new Pair<>(column, getColumnPositionInView(columnIdentifier)));
		getColumnModel().removeColumn(column);
		switch (getTableType()) {
			case 0 -> SettingsUtil.set(COLUMN_KEYS.getTextKey(columnIdentifier), "-1");
			case 1 -> SettingsUtil.set(COLUMN_KEYS.getRoutineKey(columnIdentifier), "-1");
		}
	}

	public void hideColumns() {
		IntStream.range(0, getColumnModel().getColumnCount()).filter(SettingsController::isColumnHide).forEach(this::hideColumn);
	}

	public void setColumn(int columnIdentifier, boolean toHide) {
		if (!toHide) {
			showColumn(columnIdentifier);
		} else {
			hideColumn(columnIdentifier);
		}
	}

	public void moveColumns() {
		int columnIdentifier = getTable().getColumnCount() - 1;
		switch (getTableType()) {
			case 0 -> {
				while (columnIdentifier >= 0) {
					int columnPosition = Integer.parseInt(SettingsUtil.get(COLUMN_KEYS.getTextKey(columnIdentifier)).split("/")[0]);
					if (columnPosition != -1) {
						getTable().moveColumn(getColumnPositionInView(columnIdentifier), columnPosition);
						getColumnModel().getColumn(getTable().convertColumnIndexToView(columnIdentifier)).
								setPreferredWidth(Integer.parseInt(
										SettingsUtil.get(COLUMN_KEYS.getTextKey(columnIdentifier)).split("/")[1]));
					}
					columnIdentifier--;
				}
			}
			case 1 -> {
				while (columnIdentifier >= 0) {
					int columnPosition = Integer.parseInt(SettingsUtil.get(COLUMN_KEYS.getRoutineKey(columnIdentifier)).split("/")[0]);
					if (columnPosition != -1) {
						getTable().moveColumn(getColumnPositionInView(columnIdentifier), columnPosition);
						getColumnModel().getColumn(getTable().convertColumnIndexToView(columnIdentifier)).
								setPreferredWidth(Integer.parseInt(
										SettingsUtil.get(COLUMN_KEYS.getRoutineKey(columnIdentifier)).split("/")[1]));
					}
					columnIdentifier--;
				}
			}
		}
	}

	public void saveColumnPositions() {
		switch (getTableType()) {
			case 0 -> IntStream.range(0, getTable().getColumnCount()).
					forEach(i -> SettingsUtil.set(COLUMN_KEYS.getTextKey(getTable().convertColumnIndexToModel(i)),
							i + "/" + getColumnModel().getColumn(i).getWidth()));
			case 1 -> IntStream.range(0, getTable().getColumnCount()).
					forEach(i -> SettingsUtil.set(COLUMN_KEYS.getRoutineKey(getTable().convertColumnIndexToModel(i)),
							i + "/" + getColumnModel().getColumn(i).getWidth()));
		}
	}

	public void reallignColumns() {
		hideColumns();
		moveColumns();
	}

	private int getTableType() {
		return Integer.parseInt(SettingsUtil.get(new KeysRecord().tableType()));
	}
}

