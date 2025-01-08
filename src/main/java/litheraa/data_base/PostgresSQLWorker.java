package litheraa.data_base;

import litheraa.data.Routine;
import litheraa.data.Text;

import java.nio.file.Path;
import java.sql.*;
import java.sql.Date;
import java.util.*;

import static litheraa.data_base.PostgresSQLConnector.*;

public class PostgresSQLWorker {
    private static final String CREATE_TEXTS_DB = "CREATE TABLE IF NOT EXISTS Texts" +
            "(prodName VARCHAR(50), created DATE, lastModified DATE, " +
            "charsOnDate REAL DEFAULT 0, charsSoFar REAL DEFAULT 0, charsTotal REAL DEFAULT 0, " +
            "name VARCHAR(100), textPath TEXT, PRIMARY KEY (lastModified, textPath));";

    private static final String UPSERT_TEXTS = "INSERT INTO Texts(" +
            "prodName, created, lastModified, charsSoFar, name, textPath, charsOnDate) " +
            "VALUES(?, ?, ?, ?, ?, ?, " +
            "? - (SELECT charsSoFar FROM Texts WHERE lastModified <= CURRENT_DATE AND textPath = ? LIMIT 1)) " +
            "ON CONFLICT (lastModified, textPath) DO UPDATE SET " +
            "charsSoFar = EXCLUDED.charsSoFar, " +
            "charsOnDate = EXCLUDED.charsOnDate;";

    private static final String UPDATE_TEXTS_CHARS_TOTAL = "UPDATE Texts SET charsTotal = ? WHERE textPath = ?;";

    private static final String UPDATE_TEXT_NAME = "UPDATE Texts SET name = ? WHERE textPath = ?;";

    private static final String SELECT_FROM_TEXTS = "SELECT prodName, created, lastModified, " +
            "charsOnDate, charsSoFar, charsTotal, name, textPath FROM Texts;";

    private static final String SELECT_PROD_CHARS = "SELECT charsSoFar FROM Texts " +
            "WHERE lastModified < CURRENT_DATE AND textPath = ? LIMIT 1;";


    private static final String CREATE_ROUTINE_DB = "CREATE TABLE IF NOT EXISTS Routine " +
            "(date DATE UNIQUE DEFAULT CURRENT_DATE, chars REAL DEFAULT 0, textNames TEXT);";

    private static final String CREATE_TEMPORARY_TABLE = "CREATE temp TABLE RTemp AS " +
            "SELECT lastModified, SUM(charsOnDate) as chars, array_to_string(array_agg(name), E'/') as names " +
            "FROM texts GROUP BY lastModified;";

    private static final String MERGE_ROUTINE_FROM_TEMP = "MERGE INTO Routine r USING RTemp t ON r.date = t.lastModified " +
            "WHEN MATCHED THEN DO NOTHING " +
            "WHEN NOT MATCHED THEN INSERT (date, chars, textNames) VALUES (t.lastModified, t.chars, t.names);";

    private static final String SELECT_FROM_ROUTINE = "SELECT date, chars, textNames FROM Routine;";

    private static final String UPSERT_PROD = "MERGE INTO Prod p USING Texts t ON p.name = t.name " +
            "WHEN NOT MATCHED THEN INSERT (*)  VALUES (*) " +
            "WHEN MATCHED THEN INSERT INTO Prod * VALUES (*) ON CONFLICT (date) " +
            "DO UPDATE SET *;";

    private static final String SELECT_FROM_PROD = "SELECT * FROM Prod;";


    //TODO не хватает выборки по тексту, сейчас выгружаются последние обновления по всем текстам
    static final String selectTimeStamp = "SELECT textPath, TO_CHAR(tLastModified, ' HH:MI:SS YYYY/MM/DD') FROM Texts;";

    static final String selectLastProdUpdate = "SELECT date, chars FROM Prod WHERE date <= CURRENT_DATE;";

    //String setTextDirectory = "ALTER TABLE IF EXISTS Application ADD COLUMN IF NOT EXISTS text_directory TEXT;";

    public static void createTextsDB() {
        try (PreparedStatement ps = getPreparedStatement(CREATE_TEXTS_DB)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void upsertTextsDB(LinkedList<Text> data) {
        try (PreparedStatement pS = getPreparedStatement(UPSERT_TEXTS)) {
            for (var file : data) {
                pS.setString(1,file.getProdName());                                 //имя проды
                pS.setDate(2, new Date(file.getCreated().getTimeInMillis()));       //дата создания
                pS.setDate(3, new Date(file.getLastModified().getTimeInMillis()));  //последнее изменение
                pS.setDouble(4, file.getTextChars());                                  //знаки
                pS.setString(5, file.getTextName());                                //имя текста
                pS.setString(6, String.valueOf(file.getPath()));                    //путь
                pS.setDouble(7, file.getTextChars());                                  //знаки для перевода в знаки на сегодня
                pS.setString(8, String.valueOf(file.getPath()));                    //путь для проверки
                pS.addBatch();
            }
            pS.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Text> selectTextsDB() {
        ArrayList<Text> data = new ArrayList<>();
        try (PreparedStatement pS = getPreparedStatement(SELECT_FROM_TEXTS)) {
            var result = pS.executeQuery();
            while (result.next()) {
                Text text = new Text();
                text.setProdName(result.getString("prodName"));
                text.setCreated(result.getDate("created"));
                text.setLastModified(result.getTimestamp("lastModified"));
                text.setProdChars(result.getDouble("charsOnDate"));
                text.setTextChars(result.getDouble("charsSoFar"));
                text.setCharsTotal(result.getDouble("charsTotal"));
                text.setTextName(result.getString("name"));
                text.setPath(Path.of(result.getString("textPath")));
                data.add(text);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }

    public static void updateTextsCharsTotal(double newCharsTotal, Path textPath) {
        try (PreparedStatement pS = getPreparedStatement(UPDATE_TEXTS_CHARS_TOTAL)) {
            pS.setDouble(1, newCharsTotal);
            pS.setString(2, String.valueOf(textPath));
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void updateTextName(String newTextName, Path textPath) {
        try (PreparedStatement pS = getPreparedStatement(UPDATE_TEXT_NAME)) {
            pS.setString(1, newTextName);
            pS.setString(2, String.valueOf(textPath));
            pS.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static int getPreviousProdStart(Path path) {
        int previousProdStart = 0;
        try (PreparedStatement ps = getPreparedStatement(SELECT_PROD_CHARS)) {
            ps.setString(1, path.toString());
            var result = ps.executeQuery();
            if (result.next()) {
            previousProdStart = result.getInt("charsSoFar");}
        } catch (SQLException | NumberFormatException e) {
            return 0;
        }
        return previousProdStart;
    }

    public static void createRoutineDB() {
        try (PreparedStatement ps = getPreparedStatement(CREATE_ROUTINE_DB)) {
            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void upsertRoutineDB() {
        try (PreparedStatement ps1 = getPreparedStatement(CREATE_TEMPORARY_TABLE)) {
            ps1.executeUpdate();
            try (PreparedStatement ps2 = getConnection().prepareStatement(MERGE_ROUTINE_FROM_TEMP)) {
                ps2.executeUpdate();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Routine> selectRoutineDB() {
        ArrayList<Routine> data = new ArrayList<>();
        try (PreparedStatement pS = getPreparedStatement(SELECT_FROM_ROUTINE)) {
            var result = pS.executeQuery();
            while (result.next()) {
                Routine day = new Routine();
                day.setLastModified(result.getDate("date"));
                day.setProdChars(result.getDouble("chars"));
                day.setTextNames(result.getString("textNames"));
                data.add(day);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return data;
    }
}
