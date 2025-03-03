package litheraa.util.readers;

import litheraa.controller.SettingsController;
import litheraa.data.entities.Prod;
import lombok.AccessLevel;
import lombok.Getter;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

@Getter
public abstract class ReaderInterface {

	@lombok.Setter(AccessLevel.PROTECTED)
	protected Path path;

	public abstract String getFileType();
	public abstract String getFileTypeDescription();

	protected abstract StringBuilder getText();

	protected LocalDate convertToLocalDate(Date date){
		return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
	}

	public LocalDate convertToLocalDate(Long millis) {
		return convertToLocalDate(new Date(millis));
	}

	private StringBuilder trimText() {
		StringBuilder text = new StringBuilder(getText());
		char[] oldChars = new char[text.length()];
		text.getChars(0, text.length(), oldChars, 0);
		char[] newChars = new char[text.length()];
		int newLen = 0;
		for (int j = 0; j < text.length(); j++) {
			char ch = oldChars[j];
			if (ch >= 32) {
				newChars[newLen] = ch;
				newLen++;
			} else if (ch == 10) {
				newChars[newLen] = ' ';
				newLen++;
			}
		}
		String s = new String(newChars, 0, newLen);
		return new StringBuilder(s);
	}

	public Integer getCharacters() {
		return trimText().length();
	}

	public LocalDate getCreationDate() {
		try {
			return convertToLocalDate(((FileTime) Files.getAttribute(path, "creationTime")).toMillis());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public LocalDate getLastModifiedDate() {
		return convertToLocalDate(path.toFile().lastModified());
	}

	public String getProdName(Prod prod) {
		int lastStart = prod.getChars() - prod.getWritten();
		int prodNameLength = SettingsController.getProdNameLength();
		if (prod.getWritten() < prodNameLength) {
			return trimText().substring(lastStart);
		} else {
			return trimText().substring(lastStart, lastStart + --prodNameLength);
		}
	}
}
