package litheraa.controller;

import litheraa.data.entities.SelectableText;
import litheraa.data.entities.Text;
import litheraa.data.entities.Time;
import litheraa.settings.DBSettings;
import litheraa.util.ViewType;
import org.apache.commons.math3.util.Pair;

import java.time.LocalDate;
import java.util.List;

public interface ViewControllerInterface {

	void concreteView(ViewType type, LocalDate period);

	void setTextId(List<SelectableText> textId);

	List<SelectableText> getSelectableTexts();

	Pair<List<Time>, List<Text>> getModel();

	void repaint();

	void refresh();

	void setGoal(int goal, Long... timeId);

	void reset();

	litheraa.view.MainFrame getFrame();

	DBSettings getSettings();
}
