package litheraa.controller;

import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.util.ViewType;
import org.apache.commons.math3.util.Pair;

import java.util.List;

public interface ViewControllerInterface {

	void concreteView(ViewType type);

	void setTextId(String textId);

	Pair<List<Time>, List<Text>> getModel();

	void repaint();

	void refresh();

	void setGoal(int goal, Long... timeId);

	void reset();

	litheraa.view.MainFrame getFrame();
}
