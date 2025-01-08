//package Litheraa.View.Table;
//
//import javax.swing.*;
//import javax.swing.table.DefaultTableCellRenderer;
//import javax.swing.table.TableModel;
//import java.awt.*;
//
//public class HighlightRenderer extends DefaultTableCellRenderer {
//    private JTextField searchField;
//    TableModel model;
//    private boolean is;
//
//    public HighlightRenderer(JTextField searchField, TableModel model, boolean is) {
//        this.searchField = searchField;
//        this.model = model;
//        this.is = is;
//    }
//
//    @Override
//    public Component getTableCellRendererComponent(JTable table, Object value,
//                                                   boolean selected, boolean hasFocus,
//                                                   int row, int column) {
//        Component c = super.getTableCellRendererComponent(table, value,
//                selected, hasFocus, row, column);
//
//        JLabel original = (JLabel) c;
//        original.setText(model.getColumnName(column));
////        TextHighlighted label = new TextHighlighted();
//
//        JLabel label = new JLabel(model.getColumnName(column));
//        label.setFont(original.getFont());
//        label.setText(original.getText());
//        label.setBackground(original.getBackground());
//        label.setForeground(original.getForeground());
//        label.setHorizontalTextPosition(original.getHorizontalTextPosition());
//        label.setIcon(new ImageIcon("src/main/resources/filterImage.png"));
//        label.setBorder(original.getBorder());
////        label.highlightText(searchField.getText());
//        if (is) {
//            return original;
//        } else {
//            return label;
//        }
//    }
//}
