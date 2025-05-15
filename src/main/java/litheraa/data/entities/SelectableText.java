package litheraa.data.entities;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@AllArgsConstructor
public class SelectableText {
	private final Text text;
	@Setter
	private boolean isSelected;

	public static String getTextNamePresentation(List<SelectableText> textList) {
		List<SelectableText> selectedTexts = textList.stream().filter(SelectableText::isSelected).toList();

		if (selectedTexts.size() == textList.size()) {
			return "Все тексты";
		} else {
			return switch (selectedTexts.size()) {
				case 0 -> "Тексты не выбраны";
				case 1 -> selectedTexts.getFirst().getText().getName();
				default -> "Несколько текстов";
			};
		}
	}

	@Override
	public String toString() {
		return text.getName() + "=" + isSelected;
	}

}
