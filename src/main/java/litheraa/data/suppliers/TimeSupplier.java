package litheraa.data.suppliers;

import litheraa.controller.SettingsController;
import litheraa.data.entities.Time;
import litheraa.util.readers.ReaderInterface;
import lombok.AllArgsConstructor;

import java.util.function.Supplier;

@AllArgsConstructor
public class TimeSupplier implements Supplier<Time> {
	private final ReaderInterface reader;
	private static final int goal = SettingsController.getProdGoal();

	@Override
	public Time get() {
		return new Time(reader.getLastModifiedDate(), goal);
	}
}
