package litheraa.view.calendar;

import litheraa.view.util.ComponentAdjuster;
import lombok.AllArgsConstructor;

import javax.swing.*;
import java.awt.*;

@AllArgsConstructor
public class DayLabel extends JLabel implements ComponentAdjuster {
	private  final DayPanelController controller;

	@Override
	public void adjust(Font font) {
		setFont(font);
		setPreferredSize(controller.getDimension());
	}
}
