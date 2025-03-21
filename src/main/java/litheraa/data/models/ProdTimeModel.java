package litheraa.data.models;

import litheraa.controller.SettingsController;
import litheraa.data.entities.Prod;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.time.LocalDate;
import java.time.Month;
import java.util.*;

@Getter
public class ProdTimeModel extends ProdDataModel implements Iterable<ProdTimeModel.Node>{
	private int weeks = 0;
	private final int firstDay;
	private final int lastDay;
	private final Set<Node> nodes = new TreeSet<>();
	private final List<LocalDate> dates;
	private final LocalDate from;
	private final LocalDate to;
	private static final int DEFAULT = SettingsController.getProdGoal();

	public ProdTimeModel(List<Time> times, List<Text> texts, LocalDate from, LocalDate to) {
		super(times, texts);
		this.from = from;
		this.to = to;
		dates = from.datesUntil(to).toList();
		firstDay = from.getDayOfWeek().getValue();
		lastDay = to.getDayOfWeek().getValue();
		times.stream().map(Node::new).forEach(nodes::add);
		dates.stream().map(Node::new).forEach(nodes::add);
		//noinspection StatementWithEmptyBody
		while (from.plusWeeks(weeks++).isBefore(to)) {}
	}

	public Long getTimeId(int day) {
		for (Time time : times.values()) {
			if (time.getModified().getDayOfMonth() == day) return time.getId();
		}
		return 0L;
	}

	public void setGoal(int dayGoal, int... day) {
		for (int i : day) {
			for (Time time : times.values()) {
				if (time.getModified().getDayOfMonth() == i) time.setGoal(dayGoal);
			}
		}
	}

	public int getDayGoal(int day) {
		for (Time time : times.values()) {
			if (time.getModified().getDayOfMonth() == day) return time.getGoal();
		}
		return DEFAULT;
	}

	public int getWritten(int day) {
		for (Time time : times.values()) {
			if (time.getModified().getDayOfMonth() == day) return time.getWritten();
		}
		return 0;
	}

	@Override
	public @NotNull Iterator iterator() {
		return new Iterator();
	}

	public Month getMonth() {
		return from.getMonth();
	}

//	public String getTextNames(int day) {
//		return prodMap.containsKey(day) ? prodMap.get(day).
//				stream().
//				map(prod -> prod.getText().getName()).
//				collect(Collectors.joining()) : "Вы не работали!";
//	}

	public class Iterator implements java.util.Iterator<Node> {
		private final List<Node> nodes = ProdTimeModel.this.nodes.stream().toList();
		private int index = 0;
		@Override
		public boolean hasNext() {
			return index + 1 <= nodes.size();
		}

		@Override
		public Node next() {
			index++;
			return null;
		}

		public LocalDate getId() {
			return nodes.get(index).id;
		}

		public int getWritten() {
			return nodes.get(index).written;
		}

		public int getGoal() {
			return nodes.get(index).goal;
		}

		public List<Prod> getProds() {
			return nodes.get(index).prods;
		}
	}

	protected class Node implements Comparable<Node>{
		private final LocalDate id;
		private int written;
		private int goal;
		private List<Prod> prods;

		private Node (LocalDate id) {
			this.id = id;
			written = 0;
			//TODO цель по дню недели
			goal = SettingsController.getProdGoal();
		}

		private Node (Time time) {
			id = time.getModified();
			written = time.getWritten();
			goal = time.getGoal();
			prods = time.getProds();
		}

		@Override
		public String toString() {
			return "id= " + id + " ,written= " + written + " ,goal= " + goal;
		}

		@Override
		public int compareTo(@NotNull ProdTimeModel.Node o) {
			return this.id.compareTo(o.id);
		}
	}
}
