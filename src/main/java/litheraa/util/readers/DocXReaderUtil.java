package litheraa.util.readers;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Date;
import java.util.List;

public final class DocXReaderUtil extends ReaderInterface {

    private XWPFDocument getDocument(Path path) {
        try (FileInputStream fileInputStream = new FileInputStream(path.toString())) {
            return new XWPFDocument(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected StringBuilder getText(Path path) {
        List<XWPFParagraph> paragraphs = getDocument(path).getParagraphs();
        StringBuilder text = new StringBuilder();
        for (XWPFParagraph p : paragraphs) {
            text.append(p.getText());
        }
        return text;
    }

    private POIXMLProperties.ExtendedProperties getProperties(Path path) {
        return getDocument(path).getProperties().getExtendedProperties();
    }

    @Override
    public Date getCreationDate(Path path) {
        return getDocument(path).getProperties().getCoreProperties().getCreated();
    }

    @Override
    public Date getLastModifiedDate(Path path) {
        return getDocument(path).getProperties().getCoreProperties().getModified();
    }
}
