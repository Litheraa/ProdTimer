import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.Scanner;

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
            System.out.println(date);
            System.out.println(prodCounter);

            JFrame frame = new JFrame("Счетчик умных слов и мыслей");
            frame.pack();
            frame.setSize(600, 500);
            frame.setMinimumSize(new Dimension(350, 200));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            //TODO границы, отступы, красивый вид, возможно иконку

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

            for (int i = 0; i < prodCounter; ++i) {
                JTextField dateField = new JTextField(nextData[i].substring(0, nextData[i].indexOf("L")));
                dateField.setEditable(false);
                datePanel.add(dateField);
                JTextField charactersField = new JTextField(nextData[i].substring(nextData[i].indexOf("L") + 1, nextData[i].indexOf("N")));
                charactersField.setEditable(false);
                charactersPanel.add(charactersField);
                JTextField prodField = new JTextField(nextData[i].substring(nextData[i].indexOf("N") + 1));
                prodField.setEditable(false);
                prodPanel.add(prodField);
            }

                mainPanel.add(datePanel);
                mainPanel.add(charactersPanel);
                mainPanel.add(prodPanel);

                //TODO Окно для получения проды

                frame.setVisible(true);
            }
        }
    }