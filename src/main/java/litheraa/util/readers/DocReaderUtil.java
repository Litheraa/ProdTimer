package litheraa.util.readers;

import org.apache.commons.math3.util.Pair;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;

@Component
public class DocReaderUtil extends ReaderInterface{

	private HWPFDocument getDocument(Path path) {
		try (FileInputStream fileInputStream = new FileInputStream(path.toString())) {
			return new HWPFDocument(fileInputStream);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getFileType() {
		return "doc";
	}

	@Override
	public String getFileTypeDescription() {
		return "Word document (*.doc)";
	}

	@Override
	public StringBuilder getText(Path path) {
		return new StringBuilder(getParagraph(path).getValue());
	}

	private Pair<Integer, StringBuilder> getParagraph(Path path) {
		WordExtractor extractor = new WordExtractor(getDocument(path));
		StringBuilder sB = new StringBuilder();
		String[] fileData = extractor.getParagraphText();
		int i = 0;
		for (; i < fileData.length; i++)
		{
			if (fileData[i] != null) {
				sB.append(fileData[i]);
			}
		}
		return new Pair<>(i, sB);
	}

	@Override
	public Integer getCharacters(Path path) {
		return super.getCharacters(path) - getParagraph(path).getKey() + 1;
	}

	@Override
	public Date getLastModifiedDate(Path path) {
		return getDocument(path).getSummaryInformation().getLastSaveDateTime();
	}
}
