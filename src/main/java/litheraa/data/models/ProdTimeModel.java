package litheraa.data.models;

import litheraa.controller.SettingsController;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import lombok.Getter;

import java.time.LocalDate;
import java.util.List;

@Getter
public class ProdTimeModel extends ProdDataModel {
	private int weeks = 0;
	private final int firstDay;
	private final int lastDay;
	private final int[] dates;
	private final LocalDate from;
	private final LocalDate to;
	private static final int DEFAULT = SettingsController.getProdVolume();

	public ProdTimeModel(List<Time> times, List<Text> texts, LocalDate from, LocalDate to) {
		super(times, texts);
		this.from = from;
		this.to = to;
		dates = from.datesUntil(to).map(LocalDate::getDayOfMonth).mapMultiToInt((integer, intConsumer) -> intConsumer.accept(integer)).toArray();
		firstDay = from.getDayOfWeek().getValue();
		lastDay = to.getDayOfWeek().getValue();
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

//	public String getTextNames(int day) {
//		return prodMap.containsKey(day) ? prodMap.get(day).
//				stream().
//				map(prod -> prod.getText().getName()).
//				collect(Collectors.joining()) : "Вы не работали!";
//	}
}
