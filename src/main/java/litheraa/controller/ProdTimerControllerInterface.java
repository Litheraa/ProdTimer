package litheraa.controller;

import litheraa.data.RoutineOld;
import litheraa.data.TextOld;

import java.util.ArrayList;

public interface ProdTimerControllerInterface {

    void createDB();

    void setAutoStart(boolean isAutoStart);

    void fullSave();

    void saveData();

    void saveDataByTimer();

    ArrayList<TextOld> getTextsData();

    ArrayList<RoutineOld> getRoutineData();

}
