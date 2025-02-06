package litheraa.util.readers;

import org.apache.commons.compress.utils.FileNameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.*;

@Component
public class ReaderFactory {
	private static final Map<String, ReaderInterface> INTERFACE_MAP = new HashMap<>();
	private static final String STAR = "*.";


	@Autowired
	public ReaderFactory(List<ReaderInterface> readerInterfaces) {
		for (ReaderInterface readerInterface : readerInterfaces) {
			INTERFACE_MAP.put(readerInterface.getFileType(), readerInterface);
		}
	}

	public static Set<String> getKeys() {
		return INTERFACE_MAP.keySet();
	}

	public static String[] getWildCards() {
		String[] wildCards = new String[getKeys().toArray().length];
		int i = 0;
		for (String wildCard : getKeys()) {
			wildCards[i] = STAR + wildCard;
			i++;
		}
		return wildCards;
	}

	public static Collection<ReaderInterface> getReaders() {
		return INTERFACE_MAP.values();
	}

	public static String getDescriptions() {
		StringBuilder description = new StringBuilder();
		for (ReaderInterface readerInterface : INTERFACE_MAP.values()) {
			description.append(readerInterface.getFileTypeDescription()).append(", ");
		}
		return description.substring(0, description.length() - 2);
	}

	public static ReaderInterface createReader(Path file) {
		String fileType = FileNameUtils.getExtension(file);
		ReaderInterface readerInterface = INTERFACE_MAP.get(fileType);
		if (readerInterface == null) {
			throw new IllegalArgumentException("Unexpected value: " + fileType);
		}
		return readerInterface;
	}
}
