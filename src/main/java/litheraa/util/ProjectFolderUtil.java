package litheraa.util;

import lombok.AccessLevel;
import lombok.Getter;
import org.apache.commons.io.FileUtils;

import javax.imageio.ImageIO;
import javax.swing.filechooser.FileSystemView;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;

public class ProjectFolderUtil {
	@Getter(AccessLevel.PROTECTED)
	private static final String PROJECT_FOLDER = FileSystemView.getFileSystemView().getDefaultDirectory().
			toString() + "/ProdMaster";
	@Getter(AccessLevel.PROTECTED)
	private static File settingsFile = new File(PROJECT_FOLDER + "/settings");
	@Getter(AccessLevel.PROTECTED)
	private static File pathsFile = new File(PROJECT_FOLDER + "/directories");
	@Getter(AccessLevel.PROTECTED)
	private static File messageFile = new File(PROJECT_FOLDER + "/messages");
	@Getter(AccessLevel.PROTECTED)
	private static final File DEFAULT_SETTINGS = new File(PROJECT_FOLDER + "/default");
	private static final String IMAGE_FOLDER = PROJECT_FOLDER + "/images";

	static {
		Path dir = Path.of(PROJECT_FOLDER);
		Path images = Path.of(IMAGE_FOLDER);

		createFolder(dir);
		createFolder(images);
		createFile("default", PROJECT_FOLDER, "settings");
		createFile("settings", PROJECT_FOLDER);
		createFile("directories", PROJECT_FOLDER);
		createFile("messages", PROJECT_FOLDER);
		try {
			Files.setAttribute(DEFAULT_SETTINGS.toPath(), "dos:hidden", true, LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	private static void createFolder(Path folder) {
		if (Files.notExists(folder)) {
			try {
				Files.createDirectory(folder);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void createFile(String toFile, String path, String fromFile) {
		final String PATHS = path + "/" + toFile;
		Path filePath = Path.of(PATHS);
		if (Files.notExists(filePath)) {
			try {
				URL from = ProjectFolderUtil.class.getClassLoader().getResource(fromFile);
				//noinspection DataFlowIssue
				FileUtils.copyURLToFile(from, filePath.toFile());
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
	}

	private static void createFile(String file, String path) {
		createFile(file, path, file);
	}

	public static File getImage(String image) {
		final String IMAGE = image + ".png";
		createFile(IMAGE, IMAGE_FOLDER);
		return new File(IMAGE_FOLDER + "/" + IMAGE);
	}

	public static BufferedImage getBufferedImage(String image) {
		BufferedImage bI;
		try {
			bI = ImageIO.read(getImage(image));
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return bI;
	}
}