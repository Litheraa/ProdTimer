package litheraa.view.table;

import litheraa.data.RoutineEnum;
import litheraa.SettingsController;
import litheraa.data.ColumnDataTypeEnum;
import litheraa.data.TableComparator;
import litheraa.util.ViewType;
import org.jdesktop.swingx.sort.RowFilters;

import javax.swing.*;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import java.util.*;
import java.util.regex.Pattern;

public class TableSorter extends TableRowSorter<TableModel> {

    private final RowFilter<TableModel, Integer>[] filterList;
    private final ProdTimerTable table;
    private static List<SortKey> sortKeys;
    private int sortOrder = 2;

    public TableSorter(TableModel model, ProdTimerTable table) {
        super(model);
        this.table = table;
        filterList = createEmptyFilter();

        addRowSorterListener(e -> {
            SortKey sortKey = getSortKeys().getFirst();

            SettingsController.setSortedColumn(sortKey.getColumn());
            SettingsController.setSortOrder(sortKey.getSortOrder());
        });
        if (((RoutineModel) model).getExactModelType() == ViewType.ROUTINE.ordinal()) {
	        setSortable(RoutineEnum.PROD_CHARS.ordinal(), false);
            setSortable(RoutineEnum.NAMES.ordinal(), false);
        }
        sortKeys = new ArrayList<>();
        sortKeys.add(new RowSorter.SortKey(SettingsController.getSortedColumn(), SettingsController.getSortOrder()));
        setSortKeys(sortKeys);
    }

    public void setFilter(int columnNo, String value) {
        int column = columnNo;
        columnNo = table.convertColumnIndexToModel(columnNo);
        if (!value.isEmpty() && table.getColumnType(column) == ColumnDataTypeEnum.DATE) {
            String[] tempValue = value.split(" ");
            if (tempValue.length == 2) {
                RowFilter<TableModel, Integer>[] list = new RowFilter[2];
                list[0] = new DateFilter(DateFilter.COMPARISON_TYPE.AFTER, tempValue[0], columnNo);
                list[1] = new DateFilter(DateFilter.COMPARISON_TYPE.BEFORE, tempValue[1], columnNo);
                filterList[columnNo] = RowFilter.andFilter(Arrays.asList(list));
            } else {
            filterList[columnNo] = new DateFilter(DateFilter.COMPARISON_TYPE.AFTER, value, columnNo);
            }
        } else {
            filterList[columnNo] = RowFilters.regexFilter(
                    Pattern.CASE_INSENSITIVE | Pattern.UNICODE_CASE, ".*" + Pattern.quote(value) + ".*", columnNo);
        }
        setRowFilter(RowFilter.andFilter(Arrays.asList(filterList)));
    }

    @Override
    public Comparator<?> getComparator(int column) {
        return new TableComparator(table.getColumnType(column));
    }

    private RowFilter<TableModel, Integer>[] createEmptyFilter() {
        RowFilter<TableModel, Integer>[] filters = new RowFilter[table.getColumnModel().getColumnCount()];
        for (int i = 0; i < table.getColumnModel().getColumnCount(); i++) {
            filters[i] = RowFilter.regexFilter(".*", i);
        }
        return filters;
    }

    public SortOrder nextSortOrder() {
        sortOrder = ++sortOrder % 2;
        return SortOrder.values()[sortOrder];
    }
}
