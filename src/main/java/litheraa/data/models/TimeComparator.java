package litheraa.data.models;

import litheraa.data.entities.Time;

import java.util.Comparator;

public class TimeComparator implements Comparator<Time> {
	@Override
	public int compare(Time t1, Time t2) {
		return t1.getModified().compareTo(t2.getModified());
	}
}
