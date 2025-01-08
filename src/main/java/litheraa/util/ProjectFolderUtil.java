package litheraa.util;

import org.apache.commons.io.FileUtils;

import javax.swing.filechooser.FileSystemView;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;

public class ProjectFolderUtil {
    @lombok.Getter(lombok.AccessLevel.PROTECTED)
    private static final String PROJECT_FOLDER = FileSystemView.getFileSystemView().getDefaultDirectory().
            toString() + "/ProdMaster";
    private static final String SETTINGS = PROJECT_FOLDER + "/settings";
    @lombok.Getter(lombok.AccessLevel.PROTECTED)
    private static File settingsFile = new File(SETTINGS);
    private static final String PATHS = PROJECT_FOLDER + "/directories";
    @lombok.Getter(lombok.AccessLevel.PROTECTED)
    private static File pathsFile = new File(PATHS);
    private static final String IMAGE = PROJECT_FOLDER + "/writer.png";
    @lombok.Getter
    private static File imageFile = new File(IMAGE);
    private static final String MESSAGE = PROJECT_FOLDER + "/messages";
    @lombok.Getter
    private static File messageFile = new File(MESSAGE);
    @lombok.Getter
    private static final URL DEFAULT = ProjectFolderUtil.class.getClassLoader().getResource("settings");

    static {
        Path dir = Path.of(PROJECT_FOLDER);
        Path settings = Path.of(SETTINGS);
        Path paths = Path.of(PATHS);
        Path image = Path.of(IMAGE);
        Path message = Path.of(MESSAGE);
        if (Files.notExists(dir)) {
            try {
                Files.createDirectory(dir);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

        }
        if (Files.notExists(settings)) {
            try {
                URL from = ProjectFolderUtil.class.getClassLoader().getResource("settings");
	            FileUtils.copyURLToFile(DEFAULT, settings.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (Files.notExists(paths)) {
            try {
                URL from = ProjectFolderUtil.class.getClassLoader().getResource("directories");
                assert from != null;
                FileUtils.copyURLToFile(from, paths.toFile());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
        if (Files.notExists(image)) {
            try {
                URL from = ProjectFolderUtil.class.getClassLoader().getResource("writer.png");
                assert from != null;
                FileUtils.copyURLToFile(from, image.toFile());
            } catch (IOException e) {
	            throw new RuntimeException(e);
            }
        }
        if (Files.notExists(message)) {
            try {
                URL from = ProjectFolderUtil.class.getClassLoader().getResource("messages");
                assert from != null;
                FileUtils.copyURLToFile(from, message.toFile());
            } catch (IOException e) {
	            throw new RuntimeException(e);
            }
        }
    }
}