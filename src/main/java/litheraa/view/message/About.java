package litheraa.view.message;

import litheraa.view.MainFrame;
import org.jdesktop.swingx.JXLabel;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class About extends JDialog {
	private static About aboutDialog;

	public About(MainFrame mainFrame) {
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				aboutDialog.setVisible(false);
			}
		});
		setResizable(false);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
		setSize(350, 230);
		setLocationRelativeTo(mainFrame);

		String text = MessageUtil.getAbout();
		String startingText = text.substring(0, text.indexOf("<html>"));
		String hyperLink = text.substring(text.indexOf("<html>"), text.indexOf("</html>"));
		String endingText = text.substring(text.indexOf("</html>") + 7);

		JXLabel contacts = new JXLabel("ProdMaster v1.5 \n Автор: Всеволод Храмов \n Litheraa@gmail.com ");
		contacts.setTextAlignment(JXLabel.TextAlignment.CENTER);
		contacts.setSize(200, 50);
		contacts.setLineWrap(true);
		JXLabel startingLabel = new JXLabel(startingText);
		startingLabel.setTextAlignment(JXLabel.TextAlignment.CENTER);
		startingLabel.setSize(300, 50);
		startingLabel.setLineWrap(true);
		JXLabel hyperLinkLabel = getHyperLinkLabel(hyperLink);

		JXLabel endingLabel = new JXLabel(endingText);
		endingLabel.setTextAlignment(JXLabel.TextAlignment.CENTER);
		endingLabel.setBorder(BorderFactory.createEmptyBorder(0, 0, 10, 0));

		JButton ok = new JButton("Ок");
		ok.addActionListener(new AbstractAction() {
			@Override
			public void actionPerformed(ActionEvent e) {
				aboutDialog.setVisible(false);
			}
		});

		Container container = getContentPane();

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.NORTH, contacts, 0, SpringLayout.NORTH, container);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, contacts, 0, SpringLayout.HORIZONTAL_CENTER, container);
		layout.putConstraint(SpringLayout.NORTH, startingLabel, 10, SpringLayout.SOUTH, contacts);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, startingLabel, 0, SpringLayout.HORIZONTAL_CENTER, container);
		layout.putConstraint(SpringLayout.NORTH, hyperLinkLabel, 0, SpringLayout.SOUTH, startingLabel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, hyperLinkLabel, 0, SpringLayout.HORIZONTAL_CENTER, container);
		layout.putConstraint(SpringLayout.NORTH, endingLabel, 0, SpringLayout.SOUTH, hyperLinkLabel);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, endingLabel, 0, SpringLayout.HORIZONTAL_CENTER, container);
		layout.putConstraint(SpringLayout.SOUTH, ok, -10, SpringLayout.SOUTH, container);
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, ok, 0, SpringLayout.HORIZONTAL_CENTER, container);

		container.setLayout(layout);
		container.add(contacts);
		container.add(startingLabel);
		container.add(hyperLinkLabel);
		container.add(endingLabel);
		container.add(ok);

		setVisible(true);
	}

	private static @NotNull JXLabel getHyperLinkLabel(String hyperLink) {
		JXLabel hyperLinkLabel = new JXLabel(hyperLink);
		hyperLinkLabel.setTextAlignment(JXLabel.TextAlignment.CENTER);
		hyperLinkLabel.setCursor(new Cursor(Cursor.HAND_CURSOR));
		hyperLinkLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				String uri = hyperLink.substring(hyperLink.indexOf("http"), hyperLink.indexOf("title") - 2);
				try {
					Desktop.getDesktop().browse(new URI(uri));
				} catch (URISyntaxException | IOException ex) {
					JOptionPane.showMessageDialog(aboutDialog, "Ошибка при попытке открыть сайт " + uri);
				}
			}
		});
		return hyperLinkLabel;
	}

	public static void showAbout(MainFrame mainFrame) {
		if (aboutDialog == null) {
			aboutDialog = new About(mainFrame);
		} else {
			aboutDialog.setVisible(true);
		}
	}
}
