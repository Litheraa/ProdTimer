package litheraa.view.time_spinner;

import litheraa.SettingsController;
import lombok.SneakyThrows;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;

public class TimeSpinner extends JPanel {

    private static JDialog dialog;
    private static volatile TimeSpinner instance;
    private final Object[][] commas = {{""}, {""}, {":"}, {""}, {""}};
    private final Object[] fakeHeader = {""};

    private TimeSpinner() {

        TableSpinnerModel hourModel = new TableSpinnerModel(1, true);
        hourModel.setTime(SettingsController.getDeadLineTime());

        TableSpinnerModel minuteModel = new TableSpinnerModel(1, false);
        minuteModel.setTime(SettingsController.getDeadLineTime());

        SpinnerMouse hourSpinnerMouse = new SpinnerMouse(hourModel);
        SpinnerMouse minuteSpinnerMouse = new SpinnerMouse(minuteModel);

        JTable hourSpinner = new JTable(hourModel);
        hourSpinner.getColumnModel().getColumn(0).setCellRenderer(new SpinnerRenderer());
        hourSpinner.addMouseListener(hourSpinnerMouse);
        hourSpinner.addMouseWheelListener(hourSpinnerMouse);

        SpinnerRenderer minuteRenderer = new SpinnerRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(LEFT);
                return this;
            }
        };

        SpinnerRenderer centerRenderer = new SpinnerRenderer() {
            @Override
            public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
                super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
                setHorizontalAlignment(CENTER);
                return this;
            }
        };

        JTable centerTable = new JTable(commas, fakeHeader) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        centerTable.getColumnModel().getColumn(0).setCellRenderer(centerRenderer);
        centerTable.getColumnModel().getColumn(0).setPreferredWidth(16);

        JTable minuteSpinner = new JTable(minuteModel);
        minuteSpinner.getColumnModel().getColumn(0).setCellRenderer(minuteRenderer);
        minuteSpinner.addMouseListener(minuteSpinnerMouse);
        minuteSpinner.addMouseWheelListener(minuteSpinnerMouse);

        JButton button = new JButton();

        SpringLayout layout = new SpringLayout();

        setBounds(10, 10, 10 ,10);
        setLayout(layout);
        add(hourSpinner);
        add(centerTable);
        add(minuteSpinner);
        add(button);

        layout.putConstraint(SpringLayout.WEST, hourSpinner, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.WEST, centerTable, 0, SpringLayout.EAST, hourSpinner);
        layout.putConstraint(SpringLayout.WEST, minuteSpinner, 0, SpringLayout.EAST, centerTable);
        layout.putConstraint(SpringLayout.EAST, minuteSpinner, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.NORTH, button, 0, SpringLayout.SOUTH, centerTable);
        layout.putConstraint(SpringLayout.WEST, button, 0, SpringLayout.WEST, this);
        layout.putConstraint(SpringLayout.EAST, button, 0, SpringLayout.EAST, this);
        layout.putConstraint(SpringLayout.SOUTH, button, 0, SpringLayout.SOUTH, this);

        createTableVisuals();
    }

    private void createTableVisuals() {
        for (int i = 0; i < 3; i++) {
            JTable t = (JTable) getComponent(i);
            t.setRowHeight(23);
            t.setShowGrid(false);
            t.setTableHeader(null);
            t.setFocusable(false);
        }
    }

    public static void createDialog(TimeSpinner timeSpinner, Point location) {
        if (dialog == null) {
            dialog = new JDialog();
        }

        Action action = new AbstractAction() {
            @SneakyThrows
            @Override
            public void actionPerformed(ActionEvent e) {
                JTable hourTable = (JTable) timeSpinner.getComponent(0);
                JTable minuteTable = (JTable) timeSpinner.getComponent(2);
                String time = hourTable.getValueAt(2, 0) + ":" + minuteTable.getValueAt(2, 0);
                SettingsController.setDeadlineTime(time);
                dialog.dispose();
            }
        };

        JButton button = (JButton) timeSpinner.getComponent(3);
        button.setAction(action);
        button.setText("Ok");

        dialog.setTitle("Установите время");
        dialog.add(timeSpinner);
        dialog.setLocation(location);
        dialog.setSize(180, 180);
        dialog.setResizable(false);
        dialog.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        dialog.setVisible(true);
    }

    public static TimeSpinner getInstance() {
        if (instance == null) {
            synchronized (TimeSpinner.class) {
                if (instance == null) {
                    instance = new TimeSpinner();
                }
            }
        }
        return instance;
    }
}