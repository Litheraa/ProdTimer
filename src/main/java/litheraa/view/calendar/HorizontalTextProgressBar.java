package litheraa.view.calendar;

import lombok.Getter;

import javax.swing.*;
import java.awt.*;

@Getter
public class HorizontalTextProgressBar extends JProgressBar {
	private int width = 0;

	public HorizontalTextProgressBar(int orientation) {
		super(orientation);
	}

//	@Override
//	public int getOrientation() {
//		for(StackTraceElement elem : new Throwable().getStackTrace()) {
//			if(elem.getMethodName().equals( "paintText" ) || (elem.getMethodName().equals( "paintString" ))) {
//				return JProgressBar.HORIZONTAL;
//			}
//		}
//		return JProgressBar.VERTICAL;
//	}

	@Override
	public void setValue(int n) {
		super.setValue(n);
		FontMetrics fontMetrics = getFontMetrics(getFont());
		width = fontMetrics.stringWidth("100%");
		setPreferredSize(new Dimension(width, fontMetrics.getHeight()));
	}

	@Override
	public void setStringPainted(boolean b) {
		super.setStringPainted(b);
	}

}
