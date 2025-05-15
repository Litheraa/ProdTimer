package litheraa.data.models;

import litheraa.controller.SettingsController;
import litheraa.data.entities.Prod;
import litheraa.data.entities.SelectableText;
import litheraa.data.entities.Time;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

public class CalendarModel {
	private final Map<LocalDate, Node> nodes;
	private final Map<Long, SelectableText> texts;

	public CalendarModel(Pair<List<Time>, ArrayList<SelectableText>> dataPair) {
		texts = dataPair.getSecond()
				.stream()
				.collect(Collectors.toMap(sT -> sT.getText().getId(), Function.identity()));

		nodes = dataPair.getFirst()
				.stream()
				.collect(Collectors.toMap(Time::getModified, Node::new));
	}

	public List<SelectableText> getTexts() {
		return texts.values().stream().toList();
	}

	public List<LocalDate> getValidDates() {
		return nodes.keySet().stream().sorted(LocalDate::compareTo).toList();
	}

	public int getGoal(LocalDate id) {
		return nodes.getOrDefault(id, new Node(id)).goal;
	}

	public int getWritten(LocalDate id) {
		return nodes.getOrDefault(id, new Node(id)).getWritten();
	}

	protected class Node {
		private final LocalDate id;
		private int written;
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
			return getProds().orElse(Collections.emptyList())
					.stream()
					.filter(p -> texts
							.values()
							.stream()
							.filter(SelectableText::isSelected)
							.anyMatch(sT -> Objects.equals(sT.getText().getId(), p.getTextId())))
					.reduce(0, (subtotal, p2) -> subtotal + p2.getWritten(), Integer::sum);
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
