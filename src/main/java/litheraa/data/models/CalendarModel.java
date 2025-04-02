package litheraa.data.models;

import litheraa.controller.SettingsController;
import litheraa.data.entities.Prod;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Getter
public class CalendarModel {
	private final Map<LocalDate, Node> nodes;
	private long textId = Long.decode(SettingsController.getText());
	private final Map<Long, Text> texts;
	private final Map<String, Long> textIdMap;

	public CalendarModel(Pair<List<Time>, List<Text>> dataPair) {
		texts = dataPair.getSecond()
				.stream()
				.collect(Collectors.toMap(Text::getId, Function.identity()));

		textIdMap = dataPair.getSecond()
				.stream()
				.collect(Collectors.toMap(Text::getName, Text::getId));

		nodes = dataPair.getFirst()
				.stream()
				.collect(Collectors.toMap(Time::getModified, Node::new));
	}

	public int getGoal(LocalDate id) {
		return nodes.getOrDefault(id, new Node(id)).getGoal();
	}

	public int getWritten(LocalDate id) {
		return nodes.getOrDefault(id, new Node(id)).getWritten();
	}

	public String[] getTextNames() {
		return texts.values()
				.stream()
				.map(Text::getName)
				.toArray(String[]::new);
	}

	public String getTextName() {
		Text t = texts.get(textId);
		if (t != null) {
			return t.getName();
		} else {
			return "все тексты";
		}
	}

	public void setTextId(String text) {
		long l = textIdMap.getOrDefault(text, 0L);
		if (textId != l) {
			SettingsController.setText(String.valueOf(l));
		}
	}

	protected class Node {
		private final LocalDate id;
		private int written;
		@Getter
		private int goal;
		private List<Prod> prods;

		private Node(LocalDate id) {
			this.id = id;
			written = 0;
			//TODO цель по дню недели
			goal = SettingsController.getProdGoal();
		}

		private Node(Time time) {
			id = time.getModified();
			written = time.getWritten();
			goal = time.getGoal();
			prods = time.getProds();
		}

		private int getWritten() {
			if (textId != 0) {
				return getProds().orElse(Collections.emptyList())
						.stream()
						.filter(p -> p.getTextId() == textId)
						.reduce(0, (subtotal, p2) -> subtotal + p2.getWritten(), Integer::sum);
			}
			return written;
		}

		private Optional<List<Prod>> getProds() {
			return Optional.ofNullable(prods);
		}

		@Override
		public String toString() {
			return "id= " + id + " ,written= " + written + " ,goal= " + goal;
		}
	}
}
