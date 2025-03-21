package litheraa.util.readers;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.text.Paragraph;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.util.Iterator;

@Component
public class ODTReaderUtil extends ReaderInterface {

	private TextDocument getDocument() {
		try {
			FileInputStream fileInputStream = new FileInputStream(path.toString());
			return TextDocument.loadDocument(fileInputStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public String getFileType() {
		return "odt";
	}

	@Override
	public String getFileTypeDescription() {
		return "Open document (*.odt)";
	}

	@Override
	public StringBuilder getText() {
		StringBuilder text = new StringBuilder();
		for (Iterator<Paragraph> it = getDocument().getParagraphIterator(); it.hasNext(); ) {
			text.append(it.next().getTextContent());
		}
		return text;
	}
}
