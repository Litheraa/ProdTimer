package litheraa.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import litheraa.controller.ProdTimerController;
import litheraa.view.menu.MenuBar;
import lombok.Getter;
import lombok.Setter;
import org.jdesktop.swingx.JXFrame;

public class MainFrame extends JXFrame {
	@Setter
	@Getter
	private boolean isTray;
	private final JPanel contentPane = new JPanel(new GridBagLayout());

	public MainFrame(ProdTimerController controller) {
		super("  ProdMaster");
		setBackground(UIManager.getColor("TitlePane.background"));
		setIconImage(new ImageIcon("src/main/resources/Pilcrow.png").getImage());
		add(contentPane, BorderLayout.CENTER);
		add(new MenuBar(controller), BorderLayout.NORTH);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.exit();
			}
		});
	}

	public void setHeader(JComponent header) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.HORIZONTAL;
		constraints.gridx = 0;
		constraints.gridy = 0;
		if (contentPane.getComponents().length == 0) {
			contentPane.add(header, constraints);
		} else {
			contentPane.remove(0);
			contentPane.add(header, constraints);
			validate();
		}
	}

	public void setMainComponent(JComponent component) {
		GridBagConstraints constraints = new GridBagConstraints();
		constraints.fill = GridBagConstraints.BOTH;
		constraints.gridx = 0;
		constraints.gridy = 1;
		constraints.weighty = 1.0;
		constraints.weightx = 1.0;
		if (contentPane.getComponents().length == 1) {
			contentPane.add(component, constraints);
		} else {
			contentPane.remove(0);
			contentPane.add(component, constraints);
			validate();
		}
	}
}