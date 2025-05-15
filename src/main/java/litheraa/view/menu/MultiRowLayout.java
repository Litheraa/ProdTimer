package litheraa.view.menu;

import java.awt.*;

class MultiRowLayout extends FlowLayout {

	public MultiRowLayout() {
		super(FlowLayout.LEFT, 0, 0);
	}

	@Override
	public void layoutContainer(Container parent) {
		synchronized (parent.getTreeLock()) {
			Insets insets = parent.getInsets();
			int maxWidth = parent.getWidth() - (insets.left + insets.right);
			int x = insets.left;
			int y = insets.top;
			int rowHeight = 0;
			boolean isFirstRow = true;

			for (Component comp : parent.getComponents()) {
				Dimension size = comp.getPreferredSize();

				if (x + size.width > maxWidth) {
					if (!isFirstRow) {
						alignRowRight(parent, y, rowHeight, maxWidth);
					}

					y += rowHeight + getVgap();
					x = insets.left;
					rowHeight = 0;
					isFirstRow = false;
				}

				comp.setBounds(x, y, size.width, size.height);
				x += size.width + getHgap();
				rowHeight = Math.max(rowHeight, size.height);
			}

			if (!isFirstRow) {
				alignRowRight(parent, y, rowHeight, maxWidth);
			}
		}
	}

	private void alignRowRight(Container parent, int y, int rowHeight, int maxWidth) {
		int currentX = maxWidth;

		for (int i = parent.getComponentCount() - 1; i >= 0; i--) {
			Component comp = parent.getComponent(i);
			if (comp.getY() == y && comp.getHeight() == rowHeight) {
				currentX -= comp.getWidth() + getHgap();
				comp.setLocation(currentX, y);
			}
		}
	}

	@Override
	public Dimension preferredLayoutSize(Container parent) {

		synchronized (parent.getTreeLock()) {
			int width = parent.getWidth();
			if (width == 0) width = Integer.MAX_VALUE;
			int x = 0;
			int y = getVgap();
			int rowHeight = 0;

			for (Component comp : parent.getComponents()) {
				Dimension size = comp.getPreferredSize();
				if (x + size.width + getHgap() > width) {
					y += rowHeight + getVgap();
					x = 0;
					rowHeight = 0;
				}
				x += size.width + getHgap();
				rowHeight = Math.max(rowHeight, size.height);
			}
			y += rowHeight;
			return new Dimension(parent.getWidth(), y);
		}
	}
}
