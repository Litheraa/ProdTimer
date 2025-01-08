package litheraa.view.time_spinner;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class SpinnerRenderer extends DefaultTableCellRenderer {

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        switch (row) {
            case 0, 4:
                setHorizontalAlignment(RIGHT);
                setFont(new Font("serif", Font.PLAIN, 13));
                break;
            case 1, 3:
                setFont(new Font("serif", Font.PLAIN, 19));
                break;
            case 2:
                setFont(new Font("serif", Font.PLAIN, 25));
        }
        return this;
    }
}
