package litheraa.data.models;

import litheraa.data.entities.Prod;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;

import java.util.*;

public class ProdDataModel {
	protected Map<Long, Time> times = new HashMap<>();
	protected Map<Long, Text> texts = new HashMap<>();
	protected Map<Long, Map<Long, Prod>> prods = new HashMap<>();

	public ProdDataModel(List<Time> times, List<Text> texts) {
		for (Time time : times) {
			this.times.put(time.getId(), time);
			Map<Long, Prod> prods = new HashMap<>();
			for (Prod prod : time.getProds()) {
				prods.put(prod.getTextId(), prod);
			}
			this.prods.put(time.getId(), prods);
		}
		for (Text text : texts) {
			this.texts.put(text.getId(), text);
		}
	}
}