//package litheraa.data.models;
//
//import litheraa.controller.SettingsController;
//import litheraa.data.entities.Text;
//import litheraa.data.entities.Time;
//import lombok.Getter;
//import org.apache.commons.math3.util.Pair;
//
//import java.time.LocalDate;
//import java.util.List;
//import java.util.Optional;
//
//public class DataFactory {
//	@Getter
//	private LocalDate from;
//	@Getter
//	private LocalDate to;
//	private String text = SettingsController.getText();
//	private final List<Time> times;
//	private final List<Text> texts;
//
//	protected DataFactory(Pair<List<Time>, List<Text>> data) {
//		times = data.getFirst().stream().sorted(new TimeComparator()).toList();
//		texts = data.getSecond();
//		from = SettingsController.getPeriod().getFirst();
//		to = SettingsController.getPeriod().getSecond();
//	}
//
////	public Data getData(String text) {
////		long id = textNameToId(text);
////		if (id == 0L) {
////			return new TimeDataModel(times, texts);
////		}
////		return new TextDataModel(times, texts, id);
////	}
//	public Data getData() {
//		return getData(text, from, to);
//	}
//
//	public Data getData(String text) {
//		this.text = text;
//		return getData(text, from, to);
//	}
//
//	public Data getData(LocalDate from, LocalDate to) {
//		this.from = from;
//		this.to = to;
//		return getData(text, from, to);
//	}
//
//	private Data getData(String text, LocalDate from, LocalDate to) {
//		List<Time> timeList = times
//				.stream()
//				.dropWhile(time -> time.getModified().isBefore(from))
//				.filter(time -> time.getModified().isBefore(to))
//				.toList();
//
//		Optional<Data> optional = texts.stream()
//				.filter(t -> t.getName().equals(text))
//				.map(Text::getId) // Извлекаем id
//				.findFirst()
//				.map(id -> new TextDataModel(timeList, texts, id));
//
//		return optional.orElse(new TimeDataModel(timeList, texts));
//	}
//}
