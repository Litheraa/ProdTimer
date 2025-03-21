package litheraa.util.readers;

import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.util.List;

@Component
public final class DocXReaderUtil extends ReaderInterface {

    private XWPFDocument getDocument() {
        try (FileInputStream fileInputStream = new FileInputStream(path.toString())) {
            return new XWPFDocument(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getFileType() {
        return "docx";
    }

    @Override
    public String getFileTypeDescription() {
        return "Word document (*.docx)";
    }

    @Override
    protected StringBuilder getText() {
        List<XWPFParagraph> paragraphs = getDocument().getParagraphs();
        StringBuilder text = new StringBuilder();
        for (XWPFParagraph p : paragraphs) {
            text.append(p.getText());
        }
        return text;
    }

    private POIXMLProperties.ExtendedProperties getProperties() {
        return getDocument().getProperties().getExtendedProperties();
    }

    @Override
    public LocalDate getCreationDate() {
        return convertToLocalDate(getDocument().getProperties().getCoreProperties().getCreated());
    }

    @Override
    public LocalDate getLastModifiedDate() {
        return convertToLocalDate(getDocument().getProperties().getCoreProperties().getModified());
    }
}
