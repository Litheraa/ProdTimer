import javax.swing.*;
import java.awt.*;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {

        java.io.File file = new java.io.File("прода.txt");
        SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

        StringBuilder date = new StringBuilder();
        int prodLength = 0;

        StringBuilder prodName = new StringBuilder();

        try (Scanner scanner = new Scanner(new FileInputStream("прода.txt"))) {
            StringBuilder prod;
            boolean isWhitespacesEnough = false;
            while (scanner.hasNextLine()) {
                prod = new StringBuilder(scanner.nextLine());
                prodLength += prod.toString().trim().length();
                if (isWhitespacesEnough) {continue;}
                for (int i = 0, whitespaces = 0; whitespaces < 6; ++i) {
                    prodName.append(prod.charAt(i));
                    if (prod.charAt(i) == ' ') {
                        ++whitespaces;
                    }
                    if (whitespaces == 5) {isWhitespacesEnough = true;}
                }
            }

            try (FileReader dataReader = new FileReader("sys")) {
                int c;
                while ((c = dataReader.read()) != -1) {
                    date.append((char) c);
                }
            }

            String newDate = sdf.format(new Date(file.lastModified())) + "L" + prodLength + "E" + prodName + System.lineSeparator();

            try (FileWriter writer = new FileWriter("sys", true)) {
                if (!date.toString().contains(newDate)) {
                    writer.write(newDate);
                }
            }

            String[] nextData = date.toString().split(System.lineSeparator());
            int prodCounter = 0;
            while (prodCounter < nextData.length) {
                ++ prodCounter;
                }

            JFrame frame = new JFrame("Счетчик умных слов и мыслей");
            frame.pack();
            frame.setSize(600, 500);
            frame.setMinimumSize(new Dimension(350, 200));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.X_AXIS));
            frame.add(mainPanel, BorderLayout.NORTH);

            JLabel dateLabel = new JLabel("Дата");
            dateLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JTextField dateField = new JTextField(nextData[0].substring(0, nextData[0].indexOf("L")));
            dateField.setEditable(false);
            
            JPanel datePanel = new JPanel();
            datePanel.setLayout(new BoxLayout(datePanel, BoxLayout.Y_AXIS));
            datePanel.add(dateLabel);
            datePanel.add(dateField);

            JLabel charactersLabel = new JLabel("Знаки");
            charactersLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JTextField charactersField = new JTextField(nextData[0].substring(nextData[0].indexOf("L") + 1, nextData[0].indexOf("E")));
            charactersField.setEditable(false);

            JPanel charactersPanel = new JPanel();
            charactersPanel.setLayout(new BoxLayout(charactersPanel, BoxLayout.Y_AXIS));
            charactersPanel.add(charactersLabel);
            charactersPanel.add(charactersField);

            JLabel prodLabel = new JLabel("Прода");
            prodLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT);

            JTextField prodField = new JTextField(prodName.toString());
            prodField.setEditable(false);
            
            JPanel prodPanel = new JPanel();
            prodPanel.setLayout(new BoxLayout(prodPanel, BoxLayout.Y_AXIS));
            prodPanel.add(prodLabel);
            prodPanel.add(prodField);

            mainPanel.add(datePanel);
            mainPanel.add(charactersPanel);
            mainPanel.add(prodPanel);

            frame.setVisible(true);
        }
    }
}