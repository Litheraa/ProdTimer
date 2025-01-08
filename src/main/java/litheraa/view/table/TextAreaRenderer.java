package litheraa.view.table;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;
import java.awt.*;

public class TextAreaRenderer implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        JTextPane pane = new JTextPane();
                pane.setText(value.toString());
        int newHeight = (int) pane.getPreferredSize().getHeight();
        if(table.getRowHeight(row) < newHeight){
            table.setRowHeight(row, newHeight);
        }
        return pane;
    }
}
