package litheraa.view.util.fabric;

import litheraa.view.util.SizeStepAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DimensionFactory {
	private static volatile DimensionFactory instance;
	private final Map<String, Map<SizeStepAdapter.Step, Dimension>> MAP = new HashMap<>();

	public Dimension getDimension(String iUClassID, SizeStepAdapter.Step step, int sizeStep) {
		if (!MAP.containsKey(iUClassID)) {
			Map<SizeStepAdapter.Step, Dimension> dimensionMap = new EnumMap<>(SizeStepAdapter.Step.class);
			int size = sizeStep + ((sizeStep / 10) * 10) + 1;
			for (SizeStepAdapter.Step step1 : SizeStepAdapter.Step.values()) {
				dimensionMap.put(step1, new Dimension(size, size));
				size += sizeStep;
			}
			MAP.put(iUClassID, dimensionMap);
		}
		return MAP.get(iUClassID).get(step);
	}

	public static DimensionFactory getInstance() {
		if (instance == null) {
			synchronized (DimensionFactory.class) {
				if (instance == null) {
					instance = new DimensionFactory();
				}
			}
		}
		return instance;
	}
}
