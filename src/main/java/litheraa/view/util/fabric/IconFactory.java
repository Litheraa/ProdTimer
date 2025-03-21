package litheraa.view.util.fabric;

import litheraa.view.util.SizeStepAdapter;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class IconFactory {
	private final Map<String, Map<SizeStepAdapter.Step, ImageIcon>> MAP = new HashMap<>();

	public ImageIcon getIcon(String name, SizeStepAdapter.Step step, int sizeStep) {
		if (!MAP.containsKey(name)) {
			Map<SizeStepAdapter.Step, ImageIcon> iconMap = new EnumMap<>(SizeStepAdapter.Step.class);
			ClassLoader loader = IconFactory.class.getClassLoader();
			ImageIcon icon = new ImageIcon(Objects.requireNonNull(loader.getResource(name)));
			int size = sizeStep * 3;
			for (SizeStepAdapter.Step i : SizeStepAdapter.Step.values()) {
				iconMap.put(i, new ImageIcon(icon.getImage().getScaledInstance(size, size, Image.SCALE_SMOOTH)));
				size += sizeStep;
			}
			MAP.put(name, iconMap);
		}
		return MAP.get(name).get(step);
	}
}
