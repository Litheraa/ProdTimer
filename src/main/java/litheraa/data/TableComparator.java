package litheraa.data;

import litheraa.util.CalendarWrapper;

import java.util.Comparator;
import java.util.Date;

public class TableComparator implements Comparator<String> {

    private final ColumnDataTypeEnum type;

    public TableComparator(ColumnDataTypeEnum type) {
        this.type = type;
    }
    @Override
    public int compare(String o1, String o2) {
        switch (type) {
            case STRING -> {
                return o1.compareToIgnoreCase(o2);
            }
            case DATE -> {
                Date d1 = CalendarWrapper.wrapToDate(o1);
                Date d2 = CalendarWrapper.wrapToDate(o2);
                return d2.compareTo(d1);
            }
            case DOUBLE -> {
                o1 = o1.replace(",", ".");
                o2 = o2.replace(",", ".");
                return Double.compare(Double.parseDouble(o1), Double.parseDouble(o2));
            }
        }
        throw new IllegalArgumentException();
    }
}
