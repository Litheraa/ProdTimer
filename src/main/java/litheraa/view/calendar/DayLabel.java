package litheraa.view.calendar;

import litheraa.view.util.AspectRatioAdapter;
import litheraa.view.util.SizeStepAdapter;
import litheraa.view.util.fabric.ConstraintFactory;
import litheraa.view.util.fabric.DimensionFactory;
import litheraa.view.util.fabric.FontFactory;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;

public class DayLabel extends JLabel implements AdjustableComponentInterface {
	@Getter
	private final LocalDate DATE;
	private final ConstraintFactory constraintFactory = ConstraintFactory.getInstance();
	private final DimensionFactory dimensionFactory = DimensionFactory.getInstance();
	private final FontFactory fontFactory = FontFactory.getInstance();
	private Container PARENT = getParent();

	public DayLabel(LocalDate date) {
		DATE = date;
		setText(String.valueOf(date.getDayOfMonth()));
	}

	@Override
	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
		PARENT.add(this, constraintFactory.getConstraints(getUIClassID(), ratio));
	}

	@Override
	public void sizeChanged(SizeStepAdapter.Step step) {
		setMinimumSize(dimensionFactory.getDimension(getUIClassID(), step, 13));
		setFont(fontFactory.getFont(getUIClassID(), step, 7, Font.BOLD));
	}

	@Override
	public JComponent setParent(JComponent parent) {
		PARENT = parent;
		return this;
	}
}
