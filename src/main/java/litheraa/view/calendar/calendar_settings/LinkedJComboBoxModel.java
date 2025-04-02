package litheraa.view.calendar.calendar_settings;

import lombok.Setter;

import javax.swing.*;

@Setter
public class LinkedJComboBoxModel<E> extends DefaultComboBoxModel<E> {
	private Runnable runner;

	public LinkedJComboBoxModel(E[] array) {
		super(array);
	}

	@Override
	public void setSelectedItem(Object anObject) {
		super.setSelectedItem(anObject);
		runner.run();
	}
}
