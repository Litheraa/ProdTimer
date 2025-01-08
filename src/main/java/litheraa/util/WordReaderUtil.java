package litheraa.util;

import litheraa.data_base.HSQLDBWorker;
import litheraa.SettingsController;
import org.apache.poi.ooxml.POIXMLProperties;
import org.apache.poi.xwpf.usermodel.XWPFDocument;
import org.apache.poi.xwpf.usermodel.XWPFParagraph;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

public final class WordReaderUtil implements ReaderInterface {

    private XWPFDocument getDocument(Path path) {
        try (FileInputStream fileInputStream = new FileInputStream(path.toString())) {
            return new XWPFDocument(fileInputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public StringBuilder getText(Path path) {
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

    public String getProdName(Path path) {
        int lastLength = HSQLDBWorker.getPreviousProdStart(path);
        int prodNameLength = SettingsController.getProdNameLength();
        if ((getCharacters(path)) - lastLength < prodNameLength) {
            return getText(path).substring(lastLength);
        } else {
            return getText(path).substring(lastLength, lastLength + --prodNameLength);
        }
    }

    @Override
    public Integer getCharacters(Path path) {
        return getText(path).length();
    }

    @Override
    public Double getAuthorPages(Path path) {
        DecimalFormat decimalFormat = new DecimalFormat("#.###");
        return Double.valueOf(decimalFormat.format(getCharacters(path) / 40000.0));
    }

    @Override
    public Date getCreationDate(Path path) {
        return getDocument(path).getProperties().getCoreProperties().getCreated();
    }

    public Date getLastModifiedDate(Path path) {
        return getDocument(path).getProperties().getCoreProperties().getModified();
    }
}
