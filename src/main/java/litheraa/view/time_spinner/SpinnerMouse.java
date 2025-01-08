package litheraa.view.time_spinner;

import javax.swing.*;
import java.awt.event.*;

public class SpinnerMouse extends MouseAdapter {
    private JTable table;
    int column;
    private final TableSpinnerModel model;

    public SpinnerMouse(TableSpinnerModel model) {
        this.model = model;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        table = (JTable) e.getSource();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        model.setData(table.getValueAt(2, column), e.getWheelRotation());
    }
}
