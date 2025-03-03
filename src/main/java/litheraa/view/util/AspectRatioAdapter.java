package litheraa.view.util;

import java.awt.*;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.math.BigDecimal;

public abstract class AspectRatioAdapter extends ComponentAdapter {
	protected static AspectRatio oldRatio = null;

	@Override
	public void componentResized(ComponentEvent e) {
		AspectRatio aspectRatio = calculateAspectRatio(e.getComponent().getSize());
		if (oldRatio != aspectRatio) {
			oldRatio = aspectRatio;
			aspectRatioChanged(aspectRatio);
		}
	}

	public AspectRatio calculateAspectRatio(Dimension d) {
		BigDecimal ratio = BigDecimal.valueOf(d.getHeight() / d.getWidth());
		return (ratio.compareTo(BigDecimal.valueOf(0.66)) < 0) ? AspectRatio.HORIZONTAL :
				ratio.compareTo(BigDecimal.valueOf(1.014)) > 0 ? AspectRatio.VERTICAL : AspectRatio.SQUARE;
	}

	protected abstract void aspectRatioChanged(AspectRatioAdapter.AspectRatio aspectRatio);

	public enum AspectRatio {
		HORIZONTAL,
		SQUARE,
		VERTICAL
	}
}
