package litheraa.util.readers;

import lombok.Setter;
import org.apache.commons.math3.util.Pair;
import org.apache.poi.hwpf.HWPFDocument;
import org.apache.poi.hwpf.extractor.WordExtractor;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;

@Component
public class DocReaderUtil extends ReaderInterface{

	private HWPFDocument getDocument() {
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
	public StringBuilder getText() {
		return new StringBuilder(getParagraph().getValue());
	}

	private Pair<Integer, StringBuilder> getParagraph() {
		WordExtractor extractor = new WordExtractor(getDocument());
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
	public Integer getCharacters() {
		return super.getCharacters() - getParagraph().getKey() + 1;
	}

	@Override
	public LocalDate getLastModifiedDate() {
		return convertToLocalDate(getDocument().getSummaryInformation().getLastSaveDateTime().getTime());
	}
}
