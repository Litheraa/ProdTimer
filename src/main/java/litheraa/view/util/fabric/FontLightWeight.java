package litheraa.view.util.fabric;

import litheraa.view.util.SizeStepAdapter;
import org.intellij.lang.annotations.MagicConstant;

import java.awt.*;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

public class FontLightWeight implements FabricI{
	private final Map<String, Map<SizeStepAdapter.Step, Font>> MAP = new HashMap<>();

	public Font getFont(String iUClassID, SizeStepAdapter.Step step, int sizeStep,
	                    @MagicConstant(intValues = {Font.PLAIN, Font.BOLD, Font.ITALIC}) int fontType) {
		if (!MAP.containsKey(iUClassID)) {
			Map<SizeStepAdapter.Step, Font> fontMap = new EnumMap<>(SizeStepAdapter.Step.class);
			long size = sizeStep * 3L;
			Font font = new Font("Aerial", fontType, (int) size);
			for (SizeStepAdapter.Step step1 : SizeStepAdapter.Step.values()) {
				fontMap.put(step1, font.deriveFont(size));
				size += sizeStep + 1;
			}
			MAP.put(iUClassID, fontMap);
		}
		return MAP.get(iUClassID).get(step);
	}
}
