package litheraa.settings;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import litheraa.view.MainFrame;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

import java.awt.*;

@SuppressWarnings("NullableProblems")
@Getter
@AllArgsConstructor
@RequiredArgsConstructor
@JsonDeserialize(using = WindowDeserializer.class)
public class WindowSettings {
	private int x;
	private int y;
	@NonNull private int width;
	@NonNull private int height;
	@NonNull private Dimension minSize;
	@NonNull private boolean isOnTop;
	@NonNull private boolean isResizable;
	@NonNull private boolean isTray;

	public WindowSettings(MainFrame frame) {
		x = frame.getX();
		y = frame.getY();
		width = frame.getWidth();
		height = frame.getHeight();
		minSize = frame.getMinimumSize();
		isOnTop = frame.isAlwaysOnTop();
		isResizable = frame.isResizable();
		isTray = frame.isTray();
	}
}
