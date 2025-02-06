package litheraa.view;

import litheraa.util.readers.ReaderInterface;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.util.Collection;

public class FileChooser extends JFileChooser {

    static {
        UIManager.put(
                "FileChooser.cancelButtonText", "Отмена");
        UIManager.put(
                "FileChooser.fileNameLabelText", "Наименование файла");
        UIManager.put(
                "FileChooser.filesOfTypeLabelText", "Типы файлов");
        UIManager.put(
                "FileChooser.lookInLabelText", "Путь");
    }

    private FileChooser(Collection<ReaderInterface> collection) {
        super("Добавить новую проду");
		for (ReaderInterface readerInterface : collection) {
			addChoosableFileFilter(new FileNameExtensionFilter(readerInterface.getFileTypeDescription(), readerInterface.getFileType()));
		}
	    setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
    }

    public static File chooseFile(MainFrame mainFrame, Collection<ReaderInterface> collection) {
        FileChooser fileChooser = new FileChooser(collection);

        if (fileChooser.showDialog(mainFrame, "Открыть") == JFileChooser.APPROVE_OPTION) {
            return fileChooser.getSelectedFile();
        } else return null;
    }
}
