package litheraa.data.models;

import litheraa.data.entities.Prod;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import lombok.Getter;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TextModel {
	private long textId;
	private List<Node> nodes;
	private List<Text> texts;
	private Map<Long, Time> times;

	public TextModel (Pair<List<Time>, List<Text>> dataPair, long textId) {
		this.textId = textId;

		nodes = dataPair
				.getSecond()
				.stream()
				.sorted(new TextComparator())
				.map(Node::new)
				.toList();

		times = dataPair.getFirst().stream().collect(Collectors.toMap(Time::getId, Function.identity()));

		Map<Long, List<Prod>> prods = dataPair.getFirst()
				.stream()
				.flatMap(t -> t.getProds().stream())
				.collect(Collectors.groupingBy(Prod::getTextId, Collectors.mapping(prod -> prod, Collectors.toList())));

		nodes.forEach(node -> node.addData(prods.get(node.id)));
	}

	public Node getText(int row) {
		return nodes.get(row);
	}

	public List<Long> getValidTexts() {
		return nodes.stream().map(n -> n.id).toList();
	}

	@Getter
	public class Node {
		private final long id;
		private String name;
		private final String path;
		private final LocalDate created;
		private LocalDate lastModified;
		private int written;
		private int goal;
		private List<Prod> prods;

		private Node(Text text) {
			this.id = text.getId();
			this.name = text.getName();
			this.path = text.getPath();
			this.created = text.getCreated();
			this.goal = text.getGoal();
		}

		private void addData(List<Prod> prods) {
			this.prods = prods;
			this.lastModified = times.get(prods.getLast().getTimeId()).getModified();
			this.written = prods.stream().reduce(0, (subtotal, p2) -> subtotal + p2.getWritten(), Integer::sum);
		}
	}
}
