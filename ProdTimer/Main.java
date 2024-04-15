package Litheraa.ProdTimer;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

import static javax.swing.SwingUtilities.getRootPane;

public class Main {
    public static void main(String[] args) throws IOException {

        java.io.File file = new java.io.File("прода.txt");

        StringBuilder prodName = new StringBuilder();

        try (Scanner scanner = new Scanner(new FileInputStream("прода.txt"))) {

            StringBuilder prod;
            boolean isWhitespacesEnough = false;
            int prodLength = 0;

            while (scanner.hasNextLine()) {
                prod = new StringBuilder(scanner.nextLine());
                prodLength += prod.toString().trim().length();
                if (isWhitespacesEnough) {
                    continue;
                }
                for (int i = 0, whitespaces = 0; whitespaces < 6; ++i) {
                    prodName.append(prod.charAt(i));
                    if (prod.charAt(i) == ' ') {
                        ++whitespaces;
                    }
                    if (whitespaces == 5) {
                        isWhitespacesEnough = true;
                    }
                }
            }

            StringBuilder date = new StringBuilder();

            try (Scanner secondScanner = new Scanner(new FileInputStream("sys.txt"))) {
                while (secondScanner.hasNextLine()) {
                    date.append(secondScanner.nextLine());
                }
            }

            //TODO Подытожить знаки за месяц

            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");
            String newDate = sdf.format(new Date(file.lastModified())) + "L" + prodLength + "N" + prodName + "E";

            try (FileWriter writer = new FileWriter("sys.txt", true)) {
                if (!Objects.requireNonNull(date.toString()).contains(newDate)) {
                    writer.write(newDate + System.lineSeparator());
                }
            }

            String[] nextData = date.toString().split("E");
            int prodCounter = 0;
            while (prodCounter < nextData.length) {
                ++prodCounter;
            }

            JFrame frame = new JFrame("Счетчик умных слов и мыслей");
            frame.pack();
            frame.setSize(400, 500);
            frame.setMinimumSize(new Dimension(300, 200));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            getRootPane(frame).setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

            JPanel menuPanel = new JPanel();
            menuPanel.setLayout(new BorderLayout());

            MenuBar menuBar = new MenuBar();
            Menu fileMenu = new Menu("Файл");

            MenuItem openItem = new MenuItem("Посмотреть последнюю проду");

            MenuItem saveItem = new MenuItem("Показать проды за период");

            MenuItem exitItem = new MenuItem("Выход");
            exitItem.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.exit(0);
                }
            });

            fileMenu.add(openItem);
            fileMenu.add(saveItem);
            fileMenu.addSeparator();
            fileMenu.add(exitItem);

            Menu settingsMenu = new Menu("Настройки");

            MenuItem colorSettings = new MenuItem("Цвет таблицы");

            MenuItem sizeSettings = new MenuItem("Размер окна");

            settingsMenu.add(colorSettings);
            settingsMenu.add(sizeSettings);

            Menu helpMenu = new Menu("Помошь");

            menuBar.add(fileMenu);
            menuBar.add(settingsMenu);
            menuBar.setHelpMenu(helpMenu);

            frame.setMenuBar(menuBar);

            //TODO внутри вертикальных панелей таблицы создать вертикальные панели с JTextField для прокрутки

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            frame.add(mainPanel, BorderLayout.NORTH);

            JLabel dateLabel = new JLabel("Дата");
            dateLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JPanel datePanel = new JPanel();
            datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
            datePanel.add(dateLabel);

            JLabel charactersLabel = new JLabel("Знаки");
            charactersLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JPanel charactersPanel = new JPanel();
            charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.Y_AXIS));
            charactersPanel.add(charactersLabel);

            JLabel prodLabel = new JLabel("Прода");
            prodLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JPanel prodPanel = new JPanel();
            prodPanel.setLayout(new BoxLayout(prodPanel, BoxLayout.Y_AXIS));
            prodPanel.add(prodLabel);

            JScrollPane scrollPane = new JScrollPane(prodPanel);
            scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);

            Color lightYellow = new Color(254, 248, 227);
            Color coldWhite = new Color(255,255,255);

            for (int i = 0; i < prodCounter; ++i) {
                JTextField dateField = new JTextField(nextData[i].substring(0, nextData[i].indexOf("L")));
                dateField.setEditable(false);
                dateField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.gray));
                dateField.setBackground(coldWhite);
                datePanel.add(dateField);

                JTextField charactersField = new JTextField(nextData[i].substring(nextData[i].indexOf("L") + 1, nextData[i].indexOf("N")));
                charactersField.setEditable(false);
                charactersField.setBorder(BorderFactory.createMatteBorder(1, 0, 0, 0, Color.gray));
                charactersField.setBackground(coldWhite);
                charactersPanel.add(charactersField);

                JTextField prodField = new JTextField(nextData[i].substring(nextData[i].indexOf("N") + 1));
                prodField.setEditable(false);
                prodField.setBorder(BorderFactory.createMatteBorder(1, 1, 0, 1, Color.gray));
                prodField.setBackground(coldWhite);
                prodPanel.add(prodField);

                if (i % 2 == 0) {
                    dateField.setBackground(lightYellow);
                }

                if (i == prodCounter - 1) {
                    dateField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
                    charactersField.setBorder(BorderFactory.createMatteBorder(1, 0, 1, 0, Color.gray));
                    prodField.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.gray));
                }
            }

            scrollPane.revalidate();

            mainPanel.add(datePanel);
            mainPanel.add(charactersPanel);
            mainPanel.add(prodPanel);

            //TODO Окно для получения проды
            //TODO Папки и классы

            frame.setVisible(true);
        }
    }
}