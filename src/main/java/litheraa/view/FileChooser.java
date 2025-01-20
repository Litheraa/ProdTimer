package litheraa.view;

import litheraa.ProdTimerController;
import litheraa.SettingsController;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
//TODO вертикальная полоса прокрутки
public class FileChooser extends JFileChooser {

    private static final FileNameExtensionFilter FILTER =
            new FileNameExtensionFilter("Word document (*.docx)", "docx");

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
