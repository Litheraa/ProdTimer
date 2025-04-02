package litheraa.view.calendar.calendar_settings;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.swing.*;

@Setter
@NoArgsConstructor
public class LinkedJComboBox<E> extends JComboBox<E> {
	private MutableText text;

	public LinkedJComboBox(DefaultComboBoxModel<E> model) {
		super(model);
	}

	@Override
	public void setSelectedItem(Object anObject) {
		super.setSelectedItem(anObject);
		text.mutate();
	}
}
