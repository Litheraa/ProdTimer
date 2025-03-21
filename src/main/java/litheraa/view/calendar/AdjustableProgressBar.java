//package litheraa.view.calendar;
//
//import litheraa.view.util.AspectRatioAdapter;
//import litheraa.view.util.SizeStepAdapter;
//import litheraa.view.util.fabric.ConstraintFactory;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class AdjustableProgressBar extends AbstractAdjustableComponent<JProgressBar>{
//	private final ConstraintFactory CONSTRAINT_FABRIC;
//	private final Container PARENT = SOURCE.getParent();
//
//	public AdjustableProgressBar(JProgressBar progressBar, ConstraintFactory constraintFabric) {
//		super(progressBar);
//		CONSTRAINT_FABRIC = constraintFabric;
//	}
//
//	@Override
//	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
//		if (ratio == AspectRatioAdapter.AspectRatio.VERTICAL) {
//			SOURCE.setOrientation(JProgressBar.HORIZONTAL);
//		} else SOURCE.setOrientation(JProgressBar.VERTICAL);
//		PARENT.add(SOURCE, CONSTRAINT_FABRIC.getConstraints(SOURCE.getUIClassID(), ratio));
//	}
//
//	@Override
//	public void sizeChanged(SizeStepAdapter.Step step) {}
//}
