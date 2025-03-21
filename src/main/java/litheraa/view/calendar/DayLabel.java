package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.ConstraintFactory;
import litheraa.view.util.fabric.DimensionLightWeight;
import litheraa.view.util.fabric.FontLightWeight;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DayLabel extends JLabel implements AdjustableComponentInterface {
	private final LocalDate DATE;
	private final ConstraintFactory CONSTRAINT_FABRIC;
	private final DimensionLightWeight DIMENSION_FABRIC;
	private final FontLightWeight FONT_FABRIC;
	private Container PARENT = getParent();

	public DayLabel(LocalDate date, ConstraintFactory constraintFactory, DimensionLightWeight dimensionLightWeight, FontLightWeight fontLightWeight) {
		DATE = date;
		CONSTRAINT_FABRIC = constraintFactory;
		DIMENSION_FABRIC = dimensionLightWeight;
		FONT_FABRIC = fontLightWeight;

		setText(String.valueOf(date.getDayOfMonth()));
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		PARENT.add(this, CONSTRAINT_FABRIC.getConstraints(getUIClassID(), ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		setPreferredSize(DIMENSION_FABRIC.getDimension(getUIClassID(), step, 13));
		setFont(FONT_FABRIC.getFont(getUIClassID(), step, 7, Font.BOLD));
	}

	@Override
	public void wireWithParent(JComponent parent) {
		PARENT = parent;
		PARENT.add(this);
	}
}
