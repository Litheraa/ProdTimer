package litheraa.view.util.factory;

import litheraa.view.util.SizeStepAdapter;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import javax.swing.*;
import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class IconFactory {
	private static volatile IconFactory instance;
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

	public static IconFactory getInstance() {
		if (instance == null) {
			synchronized (IconFactory.class) {
				if (instance == null) {
					instance = new IconFactory();
				}
			}
		}
		return instance;
	}
}
