package litheraa.view.calendar.calendar_settings;

import javax.swing.*;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class MutableText {
	private final JLabel label;
	private final List<JComboBox> comboBoxes = new LinkedList<>();


	public MutableText(JLabel label, JComboBox... comboBox) {
		this.label = label;
		Collections.addAll(comboBoxes, comboBox);
	}

	public void mutate() {
		StringBuilder stringBuilder = new StringBuilder();
		for (int i = comboBoxes.size() - 1; i >= 0; i--) {
			stringBuilder.append(comboBoxes.get(i).getSelectedItem().toString()).append(" ");
		}
		label.setText(stringBuilder.toString());
	}
}
