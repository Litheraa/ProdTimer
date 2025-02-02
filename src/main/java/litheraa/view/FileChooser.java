package litheraa.view;

import litheraa.ProdTimerController;
import litheraa.SettingsController;
import litheraa.util.readers.FileTypes;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

public class FileChooser extends JFileChooser {
/// There's strange bug in FileNameExtensionFilter. It may not set extensions to filter while it's an argument in array form.
///  Instead I forced to set extensions one by one
    private static final FileTypes F = new FileTypes();
    private static final FileNameExtensionFilter FILTER =
            new FileNameExtensionFilter(F.getFullNames(), F.docx(), F.doc(), F.odt());

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

    private FileChooser(String name) {
        super(name);
    }

    public static void chooseFile(MainFrame mainFrame, ProdTimerController controller) {
        JFileChooser fileChooser = new JFileChooser("Добавить новую проду");
        fileChooser.setFileFilter(FILTER);
        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);

        if (fileChooser.showDialog(mainFrame, "Открыть") == JFileChooser.APPROVE_OPTION) {
            SettingsController.setProdDirectory(fileChooser.getSelectedFile());
            controller.refresh();
        }
    }
}
