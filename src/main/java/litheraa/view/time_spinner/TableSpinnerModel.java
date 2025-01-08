package litheraa.view.time_spinner;

import javax.swing.table.AbstractTableModel;
import java.awt.*;
import java.text.DecimalFormat;

public class TableSpinnerModel extends AbstractTableModel {

    private final DecimalFormat format = new DecimalFormat("00");
    private final Object[] data = new Object[5];
    private final int step;
    private final boolean isHours;

    public TableSpinnerModel(int step, boolean isHours) {
        this.step = step;
        this.isHours = isHours;
    }

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return 1;
    }

    @Override
    public boolean isCellEditable(int row, int col) {
        return row == 2;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex];
    }

    @Override
    public void setValueAt(Object value, int row, int column) {
        String filter = (String) value;
        if (filter.matches("^[0-9]+")) {
            setData(value, 0);
        } else Toolkit.getDefaultToolkit().beep();
    }

    public void setData(Object initialValue, int stepMovement) {
        int newValue = Integer.parseInt((String) initialValue) + (step * stepMovement);
        if (isHours) {
            data[0] = format.format(Math.abs(((newValue + 24) - (step * 2)) % 24));
            data[1] = format.format(Math.abs(((newValue + 24) - step) % 24));
            data[2] = format.format(Math.abs((newValue + 24) % 24));
            data[3] = format.format(Math.abs((newValue + step) % 24));
            data[4] = format.format(Math.abs((newValue + (step * 2)) % 24));
        } else {
            data[0] = format.format(Math.abs(((newValue + 60) - (step * 2)) % 60));
            data[1] = format.format(Math.abs(((newValue + 60) - step) % 60));
            data[2] = format.format(Math.abs((newValue + 60) % 60));
            data[3] = format.format(Math.abs((newValue + step) % 60));
            data[4] = format.format(Math.abs((newValue + (step * 2)) % 60));
        }
        fireTableDataChanged();
    }

    public void setTime(String startingTime) {
        String[] time = startingTime.split(":");
        if (isHours) {setData(time[0], 0);
        } else setData(time[1], 0);
    }
}
