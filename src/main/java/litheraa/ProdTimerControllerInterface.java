package litheraa;

import litheraa.data.Routine;
import litheraa.data.Text;

import java.util.ArrayList;

public interface ProdTimerControllerInterface {

    void createDB();

    void setAutoStart(boolean isAutoStart);

    void fullSave();

    void saveData();

    void saveDataByTimer();

    ArrayList<Text> getTextsData();

    ArrayList<Routine> getRoutineData();

}
