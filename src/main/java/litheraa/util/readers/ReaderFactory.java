package litheraa.util.readers;

import org.apache.commons.compress.utils.FileNameUtils;

import java.nio.file.Path;

public class ReaderFactory{

	public static ReaderInterface createReader(Path file) {
		String fileType = FileNameUtils.getExtension(file);
		return switch (fileType) {
			case "docx" -> new DocXReaderUtil();
			case "odt" -> new ODFReaderUtil();
			case "doc" -> new DocReaderUtil();
			default -> throw new IllegalStateException("Unexpected value: " + fileType);
		};
	}
}
