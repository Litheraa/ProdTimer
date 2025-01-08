package litheraa;

import litheraa.data.Text;
import litheraa.util.WordReaderUtil;
import lombok.Setter;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.util.*;

public class Core {

	private static final WordReaderUtil wordReader = new WordReaderUtil();
	@Setter
	private static ProdTimerController controller;

	// ищет и добавляет файлы поштучно
	private static LinkedList<Path> findProd(Path startPath) {
		LinkedList<Path> paths = new LinkedList<>();
		Iterator<File> path = FileUtils.iterateFiles(
				startPath.toFile(),
				WildcardFileFilter.builder().setWildcards("*.docx").get(),
				TrueFileFilter.INSTANCE);
		while (path.hasNext()) {
			Path p = path.next().toPath();
			if (!p.getFileName().toString().startsWith("~$")) {
				paths.add(p);
			}
		}
		return paths;
	}

	// список ВСЕХ найденных файлов
	public static LinkedList<Path> findProd(LinkedList<Path> directories) {
		LinkedList<Path> files = new LinkedList<>();
		for (Path directory : directories) files.addAll(findProd(directory));
		if (files.isEmpty()) {
			return null;
		}
		return files;
	}

	public static LinkedList<Text> collectTextsData(LinkedList<Path> files) {
		try {
			Iterator<Path> iterator = Objects.requireNonNull(findProd(files)).iterator();

			LinkedList<Text> data = new LinkedList<>();
			while (iterator.hasNext()) {
				Path file = iterator.next();
				Text text = new Text();
				text.setProdName(wordReader.getProdName(file));
				text.setCreated(wordReader.getCreationDate(file));
				text.setLastModified(wordReader.getLastModifiedDate(file));
				text.setTextChars(wordReader.getCharacters(file));
				text.setTextName(String.valueOf(file.getFileName()));
				text.setPath(file);
				data.add(text);
			}
			return data;
		} catch (NullPointerException e) {
			controller.notFilesFound();
		}
		return null;
	}

	public static void autoRun(boolean isAutoRun) {
		String[] s = new String[1];
		if (isAutoRun) {
			s[0] = "cmd /C reg add HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run /v " +
					"MySquperApp /t REG_SZ /d \"%PROGRAMFILES%\\MySquperApp\\MySquperApp.lnk\" /f";
		} else {
			s[0] = "cmd /C reg delete HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Run " +
					"/v MySquperApp /f\r\n";
		}
		try {
			Runtime.getRuntime().exec(s);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
