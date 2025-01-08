package litheraa.data;

import lombok.Getter;
import lombok.Setter;

import java.nio.file.Path;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
public class Text extends Routine {

    @Setter
    private String prodName;
    private Calendar created;
    private Calendar lastModified;
    @Setter
    private double prodChars;
    @Setter
    private double textChars;
    @Setter
    private double charsTotal;
    @Setter
    private String textName;
    @Setter
    private Path path;


    public void setCreated(Date created) {
        this.created = new GregorianCalendar();
        this.created.setTime(created);
    }

    public void setLastModified(Date lastModified) {
        this.lastModified = new GregorianCalendar();
        this.lastModified.setTime(lastModified);
    }

    public String toString() {
        return "Text(prodName=" + this.getProdName() + ", created=" + this.getCreated().getTime() + ", lastModified=" + this.getLastModified().getTime() + ", prodChars=" + this.getProdChars() + ", textChars=" + this.getTextChars() + ", charsTotal=" + this.getCharsTotal() + ", textName=" + this.getTextName() + ", path=" + this.getPath() + ")";
    }
}
