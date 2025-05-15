package litheraa.util.readers;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.stereotype.Component;
import java.io.File;
import java.io.IOException;

@Component
public class ODTReaderUtil extends ReaderInterface {
	private final Tika tika = new Tika();

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
		try {
			String content = tika.parseToString(new File(path.toString()));
			return new StringBuilder(content);
		} catch (IOException | TikaException e) {
			throw new RuntimeException("Ошибка при чтении .odt файла через Tika", e);
		}
	}
}
