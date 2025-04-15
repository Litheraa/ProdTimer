package litheraa.view.util;

import litheraa.view.calendar.AdjustableComponentInterface;
import litheraa.view.calendar.CalendarPanel;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;

public class AspectRatioAdapter extends ComponentAdapter {
	private final AdjustableComponentInterface controller;
	private AspectRatio oldRatio;

	public AspectRatioAdapter(CalendarPanel controller) {
		this.controller = controller;
	}

	@Override
	public void componentResized(ComponentEvent e) {
		AspectRatio aspectRatio = calculateAspectRatio(e.getComponent().getSize());
		if (oldRatio != aspectRatio) {
			oldRatio = aspectRatio;
			controller.aspectRatioChanged(aspectRatio);
		}
	}

	private AspectRatio calculateAspectRatio(Dimension d) {
		BigDecimal ratio = BigDecimal.valueOf(d.getHeight() / d.getWidth());
		return (ratio.compareTo(BigDecimal.valueOf(0.66)) < 0) ? AspectRatio.HORIZONTAL :
				ratio.compareTo(BigDecimal.valueOf(1.08)) > 0 ? AspectRatio.VERTICAL : AspectRatio.SQUARE;
	}

	public enum AspectRatio {
		HORIZONTAL,
		SQUARE,
		VERTICAL
	}
}
