package litheraa.view.util.fabric;

import litheraa.view.util.AspectRatioAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class ConstraintFactory {
	private static volatile ConstraintFactory instance;
	private final Map<String, Map<AspectRatioAdapter.AspectRatio, GridBagConstraints>> MAP = new HashMap<>();

	public GridBagConstraints getConstraints(String iUClassId, AspectRatioAdapter.AspectRatio ratio) {
		if (!MAP.containsKey(iUClassId)) {
			Map<AspectRatioAdapter.AspectRatio, GridBagConstraints> constraintsMap = new EnumMap<>(AspectRatioAdapter.AspectRatio.class);
			switch (iUClassId) {
				case "LabelUI": {
					GridBagConstraints constraint = new GridBagConstraints();
					constraint.fill = GridBagConstraints.BOTH;
					constraint.gridy = 0;
					constraint.gridx = 0;
					for (AspectRatioAdapter.AspectRatio aspectRatio : AspectRatioAdapter.AspectRatio.values()) {
						constraintsMap.put(aspectRatio, constraint);
					}
					break;
				}
				case "PanelUI": {
					GridBagConstraints horizontalAndSquare = new GridBagConstraints();
					horizontalAndSquare.fill = GridBagConstraints.BOTH;
					horizontalAndSquare.gridy = 0;
					horizontalAndSquare.gridx = 1;
					horizontalAndSquare.weightx = 0.5;
					horizontalAndSquare.weighty = 0.5;

					GridBagConstraints vertical = new GridBagConstraints();
					vertical.fill = GridBagConstraints.BOTH;
					vertical.gridy = 1;
					vertical.gridx = 0;
					vertical.weightx = 0.5;
					vertical.weighty = 0.5;

					constraintsMap.put(AspectRatioAdapter.AspectRatio.HORIZONTAL, horizontalAndSquare);
					constraintsMap.put(AspectRatioAdapter.AspectRatio.SQUARE, horizontalAndSquare);
					constraintsMap.put(AspectRatioAdapter.AspectRatio.VERTICAL, vertical);

					break;
				}
				case "ProgressBarUI": {
					GridBagConstraints horizontal = new GridBagConstraints();
					horizontal.fill = GridBagConstraints.VERTICAL;
					horizontal.gridy = 0;
					horizontal.gridx = 2;
					horizontal.gridwidth = GridBagConstraints.REMAINDER;

					GridBagConstraints square = new GridBagConstraints();
					square.fill = GridBagConstraints.HORIZONTAL;
					square.gridy = 1;
					square.gridx = 0;
					square.gridwidth = GridBagConstraints.REMAINDER;

					GridBagConstraints vertical = new GridBagConstraints();
					vertical.fill = GridBagConstraints.HORIZONTAL;
					vertical.gridy = 2;
					vertical.gridx = 0;
					vertical.gridwidth = GridBagConstraints.REMAINDER;

					constraintsMap.put(AspectRatioAdapter.AspectRatio.HORIZONTAL, horizontal);
					constraintsMap.put(AspectRatioAdapter.AspectRatio.SQUARE, square);
					constraintsMap.put(AspectRatioAdapter.AspectRatio.VERTICAL, vertical);

					break;
				}
				case null, default: throw new IllegalArgumentException("Unknown IUClassID " + iUClassId);
			}
			MAP.put(iUClassId, constraintsMap);
		}
		return MAP.get(iUClassId).get(ratio);
	}

	public static ConstraintFactory getInstance() {
		if (instance == null) {
			synchronized (ConstraintFactory.class) {
				if (instance == null) {
					instance = new ConstraintFactory();
				}
			}
		}
		return instance;
	}
}
