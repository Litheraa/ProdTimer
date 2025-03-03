package litheraa.data.suppliers;

import litheraa.data.entities.Text;
import litheraa.util.readers.ReaderInterface;
import lombok.AllArgsConstructor;

import java.nio.file.Path;
import java.util.function.Supplier;

@AllArgsConstructor
public class TextSupplier implements Supplier<Text> {
	private final ReaderInterface reader;

	@Override
	public Text get() {
		Path path = reader.getPath();
		Text text = new Text(reader.getCreationDate(), path);
		text.setName(path.toFile().getName());
		return text;
	}
}
