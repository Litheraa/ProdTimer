package litheraa.data_base;

import litheraa.SettingsController;
import litheraa.data.calendar.Calendar;
import litheraa.data.Routine;
import litheraa.data.Text;
import litheraa.util.CalendarWrapper;

import java.nio.file.Path;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.GregorianCalendar;
import java.util.LinkedList;

import static litheraa.data_base.HSQLDBConnector.*;

public class HSQLDBWorker {
	private static final String CREATE_TEXTS = "CREATE TABLE IF NOT EXISTS Texts" +
			"(prodName VARCHAR(50), created DATE, lastModified DATE, " +
			"charsOnDate REAL DEFAULT 0, charsSoFar REAL , charsTotal REAL DEFAULT 0, " +
			"name VARCHAR(100), textPath VARCHAR(500), PRIMARY KEY (lastModified, textPath))";

	private static final String CREATE_TEXTS_TEMP_TABLE = "DECLARE LOCAL TEMPORARY TABLE TempT" +
			"(prodName VARCHAR(50), created DATE, lastModified DATE, charsSoFar REAL, " +
			"name VARCHAR(100), textPath VARCHAR(500), charsOnDate REAL) " +
			"ON COMMIT PRESERVE ROWS";

	private static final String INSERT_TEMP = "INSERT INTO SESSION.TempT VALUES (?, ?, ?, ?, ?, ?, " +
			"? - (COALESCE ((SELECT charsSoFar FROM Texts " +
			"WHERE lastModified <= CURRENT_DATE AND textPath = ? ORDER BY lastModified DESC LIMIT 1), 0)))";

	private static final String MERGE_TEXTS_FROM_TEMP = "MERGE INTO Texts USING SESSION.TempT AS T ON " +
			"texts.lastModified = T.lastModified AND texts.textPath = T.textPath " +
			"WHEN MATCHED THEN UPDATE SET charsSoFar = T.charsSoFar, charsOnDate = (texts.charsOnDate + T.charsOnDate) " +
			"WHEN NOT MATCHED THEN INSERT (prodName, created, lastModified, charsSoFar, name, textPath, charsOnDate) " +
			"VALUES (T.prodName, T.created, T.lastModified, T.charsSoFar, T.name, T.textPath, T.charsOnDate)";

	private static final String UPDATE_TEXTS_CHARS_TOTAL = "UPDATE Texts SET charsTotal = ? WHERE textPath = ?";

	private static final String UPDATE_TEXT_NAME = "UPDATE Texts SET name = ? WHERE textPath = ?";

	private static final String SELECT_TEXTS = "SELECT prodName, created, lastModified, " +
			"charsOnDate, charsSoFar, charsTotal, name, textPath FROM Texts WHERE created >= ?";

	private static final String SELECT_PROD_CHARS = "SELECT charsSoFar FROM Texts " +
			"WHERE lastModified < CURRENT_DATE AND textPath = ? LIMIT 1";

	private static final String CREATE_ROUTINE = "CREATE TABLE IF NOT EXISTS Routine " +
			"(date DATE DEFAULT CURRENT_DATE UNIQUE, chars REAL DEFAULT 0, charGoal INTEGER, textNames VARCHAR(500))";

	private static final String CREATE_ROUTINE_TEMP_TABLE = "DECLARE LOCAL TEMPORARY TABLE RTemp AS " +
			"(SELECT lastModified, SUM(charsOnDate) AS chars, STRING_AGG(name, '/') AS names " +
			"FROM Texts GROUP BY lastModified) " +
			"WITH DATA ON COMMIT PRESERVE ROWS";

	private static final String MERGE_ROUTINE_FROM_TEMP = "MERGE INTO Routine r USING RTemp t ON r.date = t.lastModified " +
			"WHEN MATCHED THEN UPDATE SET chars = t.chars, textNames = t.names " +
			"WHEN NOT MATCHED THEN INSERT (date, chars, textNames) VALUES (t.lastModified, t.chars, t.names)";

	private static final String SELECT_ROUTINE = "SELECT date, chars, textNames FROM Routine WHERE date >= ?";

	private static final String SELECT_CALENDAR =  "SELECT date, chars, charGoal, textNames FROM Routine WHERE date LIKE ?";

	private static final String UPDATE_CALENDAR = "UPDATE ROUTINE SET charGoal = ? WHERE date LIKE ?";

	private static final String SELECT_AVAILABLE_YEARS = "SELECT EXTRACT (YEAR FROM date) AS UniqueYear FROM ROUTINE GROUP BY UniqueYear";

	private static final String SELECT_CHARS = "SELECT chars FROM Routine WHERE date = CURRENT_DATE ORDER BY date DESC LIMIT 1";

	public static void createTexts() {
		try (PreparedStatement ps = getPreparedStatement(CREATE_TEXTS)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void upsertTexts(LinkedList<Text> data) {
		try (PreparedStatement pS1 = getPreparedStatement(CREATE_TEXTS_TEMP_TABLE)) {
			pS1.executeUpdate();
			try (PreparedStatement pS2 = getConnection().prepareStatement(INSERT_TEMP)) {
				for (var file : data) {
					pS2.setString(1, file.getProdName());                                //имя проды
					pS2.setDate(2, new Date(file.getCreated().getTimeInMillis()));       //дата создания
					pS2.setDate(3, new Date(file.getLastModified().getTimeInMillis()));  //последнее изменение
					pS2.setDouble(4, file.getTextChars());                               //знаки
					pS2.setString(5, file.getTextName());                                //имя текста
					pS2.setString(6, String.valueOf(file.getPath()));                    //путь
					pS2.setDouble(7, file.getTextChars());                               //знаки для перевода в знаки на сегодня
					pS2.setString(8, String.valueOf(file.getPath()));                    //путь для проверки
					pS2.addBatch();
				}
				pS2.executeBatch();
				try (PreparedStatement pS3 = getConnection().prepareStatement(MERGE_TEXTS_FROM_TEMP)) {
					pS3.executeUpdate();
				}
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static ArrayList<Text> selectTexts() {
		ArrayList<Text> data = new ArrayList<>();
		try (PreparedStatement pS = getPreparedStatement(SELECT_TEXTS)) {
			pS.setDate(1, CalendarWrapper.wrapToSQLDate(SettingsController.getCutDate()));
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
				previousProdStart = result.getInt("charsSoFar");
			}
		} catch (SQLException | NumberFormatException e) {
			return 0;
		}
		return previousProdStart;
	}

	public static void createRoutine() {
		try (PreparedStatement ps = getPreparedStatement(CREATE_ROUTINE)) {
			ps.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static void upsertRoutine() {
		try (PreparedStatement ps1 = getPreparedStatement(CREATE_ROUTINE_TEMP_TABLE)) {
			ps1.executeUpdate();
			try (PreparedStatement ps2 = getConnection().prepareStatement(MERGE_ROUTINE_FROM_TEMP)) {
				ps2.executeUpdate();
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static ArrayList<Routine> selectRoutine() {
		ArrayList<Routine> data = new ArrayList<>();
		try (PreparedStatement pS = getPreparedStatement(SELECT_ROUTINE)) {
			pS.setDate(1, CalendarWrapper.wrapToSQLDate(SettingsController.getCutDate()));
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

	public static Calendar selectCalendar(int year, int month) {
		String monthString = month < 10 ? "0" + month : String.valueOf(month);
		String date = year + "-" + monthString + "-%";
		return selectCalendar(date);
	}

	public static Calendar selectCalendar(String date) {
		Calendar calendar = new Calendar(date);
		try (PreparedStatement pS = getPreparedStatement(SELECT_CALENDAR)) {
			pS.setString(1, date);
			var result = pS.executeQuery();
			while (result.next()) {
				GregorianCalendar temp = new GregorianCalendar();
				java.util.Date resultDate = result.getDate("date");
				temp.setTime(resultDate);
				int day = temp.get(java.util.Calendar.DATE);
				calendar.setDayProgress(day, result.getDouble("chars"));
				calendar.setDayGoal(day, result.getInt("charGoal"));
				calendar.setTextNames(day, result.getString("textNames"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return calendar;
	}

	public static void updateCalendar(Calendar data, int day) {
		try (PreparedStatement pS = getPreparedStatement(UPDATE_CALENDAR)) {
			pS.setInt(1, data.getDayGoal(day));
			pS.setString(2, data.getDate(day));
			pS.executeUpdate();
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}

	public static ArrayList<Integer> selectYears() {
		ArrayList<Integer> years = new ArrayList<>();
		try (PreparedStatement pS = getPreparedStatement(SELECT_AVAILABLE_YEARS)) {
			var result = pS.executeQuery();
			while (result.next()) {
				years.add(result.getInt("UniqueYear"));
			}
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
		return years;
	}

	public static double selectTodayChars() {
		try (PreparedStatement pS = getPreparedStatement(SELECT_CHARS)) {
			var result = pS.executeQuery();
			if (result.next()) {
				return result.getDouble("chars");
			}
			return 0;
		} catch (SQLException e) {
			throw new RuntimeException(e);
		}
	}
}
