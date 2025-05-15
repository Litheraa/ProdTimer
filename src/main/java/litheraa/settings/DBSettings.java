package litheraa.settings;

import litheraa.util.MeasureUnit;
import litheraa.util.ViewType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.HashSet;
import java.util.Set;

@Setter
@Getter
public class DBSettings {
	private Set<Long> textIds = new HashSet<>();
	private Period period = new Period(LocalDate.now());
	private short updateInterval = 5;
	private LocalTime deadline = LocalTime.MIDNIGHT;
	private short length = 20;
	private MeasureUnit unit = MeasureUnit.CHAR;

	public Period getPeriod() {
		return new Period(period.getPeriodType(), period.getFrom(), period.getTo());
	}

	@Getter
	@Setter
	@AllArgsConstructor
	public static class Period {
		private ViewType periodType;
		private LocalDate from;
		private LocalDate to;

		public Period(LocalDate period) {
			from = period;
			to = period;
		}
	}
}
