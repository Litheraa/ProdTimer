package litheraa.data.models;

import litheraa.data.entities.Text;

import java.util.Comparator;

public class TextComparator implements Comparator<Text> {
	@Override
	public int compare(Text t1, Text t2) {
		return t1.getCreated().compareTo(t2.getCreated());
	}
}
