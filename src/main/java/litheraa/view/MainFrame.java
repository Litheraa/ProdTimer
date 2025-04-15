package litheraa.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import litheraa.controller.ProdTimerController;
import litheraa.view.menu.TableMenuBar;
import org.jdesktop.swingx.HorizontalLayout;

public class MainFrame extends JFrame {
	private final JPanel contentPane = new JPanel(new GridBagLayout());

	public MainFrame(ProdTimerController controller) {
		super("ProdMaster");
		JPanel menuPane = new JPanel();
		menuPane.setLayout(new HorizontalLayout());
		menuPane.add(new TableMenuBar(controller));

		add(menuPane, BorderLayout.NORTH);
		add(contentPane);

		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.exit();
			}
		});
		setLocationRelativeTo(null);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
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