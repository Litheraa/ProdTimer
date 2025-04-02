//package litheraa.data.models;
//
//import litheraa.controller.SettingsController;
//import litheraa.data.entities.Prod;
//import litheraa.data.entities.Text;
//import litheraa.data.entities.Time;
//import lombok.Setter;
//
//import java.time.LocalDate;
//import java.util.*;
//
//public class Data {
//	private int DEFAULT = SettingsController.getProdGoal();
//	protected Map<Long, Time> times = new HashMap<>();
//	protected Map<Long, Text> texts = new HashMap<>();
//	protected Map<Long, Map<Long, Prod>> prods = new HashMap<>();
//	private final Map<String, Long> nameToId = new HashMap<>();
//	@Setter
//	private LocalDate from = SettingsController.getPeriod().getFirst();
//	@Setter
//	private LocalDate to = SettingsController.getPeriod().getSecond();
//	@Setter
//	private String textName = SettingsController.getText();
//
//	protected Data(List<Time> times, List<Text> texts, LocalDate from, LocalDate to) {
//		this.from = from;
//		this.to = to;
//		for (Time time : times) {
//			this.times.put(time.getId(), time);
//			Map<Long, Prod> prods = new HashMap<>();
//			for (Prod prod : time.getProds()) {
//				prods.put(prod.getTextId(), prod);
//			}
//			this.prods.put(time.getId(), prods);
//		}
//		for (Text text : texts) {
//			this.texts.put(text.getId(), text);
//			nameToId.put(text.getName(), text.getId());
//		}
//	}
//
//	protected int getGoal(long id) {
//		return Optional.ofNullable(times.get(id).getGoal()).orElse(DEFAULT);
//	}
//
//	protected List<Time> getTimes() {
//		return times.values().stream().toList();
//	}
//
//	protected String[] getTextNames() {
//		return texts.values().stream().map(Text::getName).toArray(String[]::new);
//	}
//
//	protected int getWritten(long id) {
//		return Optional.ofNullable(prods.get(id).get(nameToId.get(textName)).getWritten();
//	}
//
//	protected List<Prod> getProds(long id) {
//
//	}
//}
