package litheraa.util;

import java.nio.file.Path;
import java.util.Date;

public interface ReaderInterface {

    Integer getCharacters(Path path);
    Double getAuthorPages(Path path);
    Date getCreationDate(Path path);
    Date getLastModifiedDate(Path path);
}
