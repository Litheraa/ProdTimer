package litheraa.view.table;

import litheraa.SettingsController;
import litheraa.util.MeasureUnit;
import litheraa.util.NumberDeclensionRu;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

public class ProgressRenderer extends DefaultTableCellRenderer {
	private final JProgressBar progressBar = new JProgressBar();
	private final int prodVolume = SettingsController.getProdVolume();

	public ProgressRenderer() {
		setOpaque(true);
		progressBar.setBorder(BorderFactory.createEmptyBorder(1, 1, 1, 1));
		progressBar.setMaximum(prodVolume);
		progressBar.setStringPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
		int actualValue = MeasureUnit.toChars(value);

		JLabel label = new JLabel("Написано: " + actualValue + " Осталось: " + (prodVolume - actualValue));
		label.setForeground(new Color(99, 130, 207));

		JTextArea container = new JTextArea();
		int newHeight = (int) (label.getPreferredSize().getHeight() + progressBar.getPreferredSize().getHeight()) + 5;
		if (table.getRowHeight(row) < newHeight) {
			table.setRowHeight(row, newHeight);
		}

		progressBar.setBorder(BorderFactory.createLineBorder(new Color(184, 207, 229), 1));

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.WIDTH, progressBar, 0, SpringLayout.WIDTH, container);
		layout.putConstraint(SpringLayout.WEST, label, 2, SpringLayout.WEST, container);
		layout.putConstraint(SpringLayout.EAST, label, -2, SpringLayout.EAST, container);
		layout.putConstraint(SpringLayout.NORTH, label, 0, SpringLayout.SOUTH, progressBar);

		container.setLayout(layout);

		if (actualValue < 0) {
			progressBar.setString("Что-то тут не так!");
		} else if (actualValue < prodVolume) {
			progressBar.setValue(actualValue);
			container.add(label);
			container.add(progressBar);
			return container;
		}
		String measure = SettingsController.isChars() ? "знак" : "Алка";
		super.getTableCellRendererComponent(table, "Готово! " + value + " " +
				NumberDeclensionRu.decline(measure.concat(" написан"), actualValue), isSelected, hasFocus, row, column);
		return this;
	}
}