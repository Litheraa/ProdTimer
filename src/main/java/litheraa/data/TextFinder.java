package litheraa.data;

import litheraa.util.readers.ReaderFactory;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.TrueFileFilter;
import org.apache.commons.io.filefilter.WildcardFileFilter;

import java.io.File;
import java.nio.file.Path;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class TextFinder {

/// ищет и добавляет файлы поштучно
	private static LinkedList<Path> findProd(Path startPath) {
		LinkedList<Path> paths = new LinkedList<>();
		Iterator<File> path = FileUtils.iterateFiles(
				startPath.toFile(),
				WildcardFileFilter.builder().setWildcards(ReaderFactory.getWildCards()).get(),
				TrueFileFilter.INSTANCE);
		while (path.hasNext()) {
			Path p = path.next().toPath();
///  в windows при открытии файла в его директории создается его копия, начинающаяся с ~$
///  к нему нельзя обратиться - ошибка доступа
			if (!p.getFileName().toString().startsWith("~$")) {
				paths.add(p);
			}
		}
		return paths;
	}

/// список ВСЕХ найденных файлов
	public static LinkedList<Path> findProd(List<Path> directories) {
		LinkedList<Path> files = new LinkedList<>();
		for (Path directory : directories) {
			files.addAll(findProd(directory));
		}
		if (files.isEmpty()) {
			throw new NullPointerException("no readable files found in directories" + directories);
		}
		return files;
	}
}
