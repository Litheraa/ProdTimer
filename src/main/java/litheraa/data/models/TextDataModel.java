package litheraa.data.models;

import litheraa.data.entities.Prod;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import lombok.Setter;

import java.util.*;

public class TextDataModel {
	private final List<Time> times;
	private final List<Text> texts;
	private final Map<Long, List<Prod>> prods = new HashMap<>();
	@Setter
	private long textId;

	public TextDataModel(List<Time> times, List<Text> texts) {
		this.times = times;
		this.texts = texts;
		for (Time time : times) {
			prods.put(time.getId(), time.getProds());
		}
	}

	public int getWritten(long id) {
		return prods.getOrDefault(id, Collections.emptyList())
				.stream()
				.reduce(0, (subtotal, p2) -> subtotal + p2.getWritten(), Integer::sum);
	}

	public List<Prod> getProds(long id) {
		return prods.getOrDefault(id, Collections.emptyList())
				.stream()
				.filter(prod -> prod.getTextId() != textId)
				.toList();
	}
}
