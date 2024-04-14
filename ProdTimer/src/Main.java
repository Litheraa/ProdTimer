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

            String newDate = sdf.format(new Date(file.lastModified())) + "L" + prodLength + "E";

            try (
                    FileWriter writer = new FileWriter("sys", true)) {
                if (!date.toString().contains(newDate)) {
                    writer.write(newDate);
                }
            }

            String[] nextData = date.toString().split("E");
            /*for (
                    int i = 0;
                    i < nextData.length; ++i) {
                System.out.println(nextData[i].substring(0, nextData[i].indexOf("L")));
                System.out.println(nextData[i].substring(nextData[i].indexOf("L") + 1));
            }*/

            JFrame frame = new JFrame("Счетчик умных слов и мыслей");
            frame.pack();
            frame.setSize(600, 500);
            frame.setMinimumSize(new Dimension(350, 200));
            frame.setLocationRelativeTo(null);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setLayout(new BorderLayout());
            final JPanel mainPanel = new JPanel();
            final JScrollPane scrollPane = new JScrollPane(mainPanel);
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            frame.add(mainPanel, BorderLayout.NORTH);
            scrollPane.revalidate();

            JLabel dateLabel = new JLabel("Дата");
            JPanel datePanel = new JPanel();
            datePanel.setPreferredSize(new Dimension(100, 5));
            datePanel.add(dateLabel);

            JLabel charactersLabel = new JLabel("Знаки");
            JPanel charactersPanel = new JPanel();
            charactersPanel.setPreferredSize(new Dimension(100, 5));
            charactersPanel.add(charactersLabel);

            JLabel prodLabel = new JLabel("Прода");
            JPanel prodPanel = new JPanel();
            prodPanel.setPreferredSize(new Dimension(100, 5));
            prodPanel.add(prodLabel);

            JTextField dateField = new JTextField(nextData[0].substring(0, nextData[0].indexOf("L")));
            dateField.setEditable(false);
            JPanel dateFieldPanel = new JPanel();
            dateFieldPanel.setPreferredSize(new Dimension(100, 20));
            dateFieldPanel.add(dateField);

            JTextField charactersField = new JTextField(nextData[0].substring(nextData[0].indexOf("L") + 1));
            charactersField.setEditable(false);
            JPanel charactersFieldPanel = new JPanel();
            charactersFieldPanel.setPreferredSize(new Dimension(100, 20));
            charactersFieldPanel.add(charactersField);

            JTextField prodField = new JTextField(prodName.toString());
            prodField.setEditable(false);
            JPanel prodFieldPanel = new JPanel();
            prodFieldPanel.setPreferredSize(new Dimension(100, 20));
            prodFieldPanel.add(prodField);

            mainPanel.add(datePanel);
            mainPanel.add(charactersPanel);
            mainPanel.add(prodPanel);

            mainPanel.add(dateFieldPanel);
            mainPanel.add(charactersFieldPanel);
            mainPanel.add(prodFieldPanel);

            frame.setVisible(true);
        }
    }
}