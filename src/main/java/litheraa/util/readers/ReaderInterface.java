package litheraa.util.readers;

import litheraa.SettingsController;
import litheraa.data_base.HSQLDBWorker;

import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.attribute.FileTime;
import java.util.Date;

public abstract class ReaderInterface {

	protected abstract StringBuilder getText(Path path);

	private StringBuilder trimText(Path path) {
		StringBuilder text = new StringBuilder(getText(path));
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

	public Integer getCharacters(Path path) {
		return trimText(path).length();
	}

	public Double getAuthorPages(Path path) {
		BigDecimal bD = BigDecimal.valueOf(getCharacters(path) / 40000.0);
		return bD.setScale(3, RoundingMode.HALF_UP).doubleValue();
	}

	public Date getCreationDate(Path path) {
		try {
			return new Date(((FileTime) Files.getAttribute(path, "creationTime")).toMillis());
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public Date getLastModifiedDate(Path path) {
		return new Date(new File(String.valueOf(path)).lastModified());
	}

	public String getProdName(Path path) {
		int lastStart = HSQLDBWorker.getPreviousProdStart(path);
		int prodNameLength = SettingsController.getProdNameLength();
		if ((getCharacters(path)) - lastStart < prodNameLength) {
			return trimText(path).substring(lastStart);
		} else {
			return trimText(path).substring(lastStart, lastStart + --prodNameLength);
		}
	}
}
