package litheraa.view.table;

import litheraa.util.CalendarWrapper;

import javax.swing.*;
import javax.swing.table.TableModel;
import java.util.Date;

public class DateFilter extends RowFilter<TableModel, Integer> {
    public enum COMPARISON_TYPE {AFTER, BEFORE, EXACT}
    private final int column;
    private final Date sourceDate;
    private final COMPARISON_TYPE type;

    public DateFilter(COMPARISON_TYPE type, String sourceDate, int column) {
        this.column = column;
        this.sourceDate = CalendarWrapper.wrapToDate(sourceDate);
        this.type = type;
    }

    @Override
    public boolean include(Entry<? extends TableModel, ? extends Integer> entry) {
        RoutineModel model = (RoutineModel) entry.getModel();
        Date date = CalendarWrapper.wrapToDate(model.getValueAt(entry.getIdentifier(), column).toString());
        return switch (type) {
            case AFTER -> Boolean.logicalOr(date.after(sourceDate), date.equals(sourceDate));
            case BEFORE -> Boolean.logicalOr(date.before(sourceDate), date.equals(sourceDate));
            case EXACT -> date.equals(sourceDate);
        };
    }
}
