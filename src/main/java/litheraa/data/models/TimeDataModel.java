//package litheraa.data.models;
//
//import litheraa.data.entities.Prod;
//import litheraa.data.entities.Text;
//import litheraa.data.entities.Time;
//
//import java.util.*;
//
//public class TimeDataModel extends Data {
//
//	public TimeDataModel(List<Time> times, List<Text> texts) {
//		super(times, texts);
//	}
//
//	public int getWritten(long id) {
//		if (id == 0) return 0;
//		return times.get(id).getWritten();
//	}
//
//	public List<Prod> getProds(long id) {
//		return prods.get(id).values().stream().toList();
//	}
//
//}
