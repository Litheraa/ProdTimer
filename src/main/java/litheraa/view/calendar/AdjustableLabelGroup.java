//package litheraa.view.calendar;
//
//import litheraa.view.util.AspectRatioAdapter;
//import litheraa.view.util.SizeStepAdapter;
//import litheraa.view.util.fabric.ConstraintFactory;
//import litheraa.view.util.fabric.FontLightWeight;
//import litheraa.view.util.fabric.IconFactory;
//
//import javax.swing.*;
//import java.awt.*;
//
//public class AdjustableLabelGroup extends AbstractAdjustableComponent<LabelGroup> {
//	private final ConstraintFactory CONSTRAINT_FABRIC;
//	private final FontLightWeight FONT_FABRIC;
//	private final IconFactory ICON_FABRIC;
//	private final Container PARENT = SOURCE.getParent();
//	private static final String[] ICON_NAMES = {"mission.png", "magic-book.png", "writed-book.png"};
//
//	public AdjustableLabelGroup(LabelGroup labelGroup, ConstraintFactory constraintFabric, FontLightWeight fontFabric, IconFactory iconFabric) {
//		super(labelGroup);
//		CONSTRAINT_FABRIC = constraintFabric;
//		FONT_FABRIC = fontFabric;
//		ICON_FABRIC = iconFabric;
//	}
//
//	@Override
//	public void aspectRatioChanged(AspectRatioAdapter.AspectRatio ratio) {
//		PARENT.add(SOURCE, CONSTRAINT_FABRIC.getConstraints(SOURCE.getUIClassID(), ratio));
//	}
//
//	@Override
//	public void sizeChanged(SizeStepAdapter.Step step) {
//		for (int i = 0; i < SOURCE.getComponentCount(); i++) {
//			JLabel label = (JLabel) SOURCE.getComponent(i);
//			label.setFont(FONT_FABRIC.getFont(SOURCE.getUIClassID(), step, 4, Font.BOLD));
//			label.setIcon(ICON_FABRIC.getIcon(ICON_NAMES[i], step, 4));
//		}
//	}
//}
