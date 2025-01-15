package litheraa.data;

import lombok.Getter;
import lombok.Setter;

import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

@Getter
public class Routine {
    private Calendar lastModified;
    @Setter
    private double prodChars;
    @Setter
    private String textNames;

    public void setLastModified(Date lastModified) {
        this.lastModified = new GregorianCalendar();
        this.lastModified.setTime(lastModified);
    }

    public String toString() {
        return "Routine(lastModified=" + this.getLastModified().getTime() + ", prodChars=" + this.getProdChars() + ", textNames=" + this.getTextNames() + ")";
    }
}
