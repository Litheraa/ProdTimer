package litheraa.util.readers;

import org.odftoolkit.simple.TextDocument;
import org.odftoolkit.simple.text.Paragraph;

import java.io.FileInputStream;
import java.nio.file.Path;
import java.util.Iterator;

public class ODFReaderUtil extends ReaderInterface {

	private TextDocument getDocument(Path path) {
		try {
			FileInputStream fileInputStream = new FileInputStream(path.toString());
			return TextDocument.loadDocument(fileInputStream);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public StringBuilder getText(Path path) {
		StringBuilder text = new StringBuilder();
		for (Iterator<Paragraph> it = getDocument(path).getParagraphIterator(); it.hasNext(); ) {
			text.append(it.next().getTextContent());
		}
		return text;
	}
}
