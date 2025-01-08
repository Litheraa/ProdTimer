package litheraa.view;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import litheraa.ProdTimerController;
import litheraa.view.table.ProdTimerTable;
import lombok.Getter;
import org.jdesktop.swingx.HorizontalLayout;
import org.jdesktop.swingx.JXLabel;

import static javax.swing.BoxLayout.*;

public class MainFrame extends JFrame {
	@Getter
	private ProdTimerTable table;
	private final JPanel jMenuPane = new JPanel();
	private JMenuBar menuBar;
	private final Box contents = new Box(Y_AXIS);

	public MainFrame() {
		super("ProdMaster");
		jMenuPane.setLayout(new HorizontalLayout());
		add(jMenuPane, BorderLayout.NORTH);
		setLocationRelativeTo(null);
		getRootPane().setBorder(BorderFactory.createEmptyBorder(0, 2, 2, 2));
		add(contents);
	}

	public MainFrame(ProdTimerController controller) {
		this();
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				controller.getViewController().exit();
			}
		});
	}

	public void setMainComponent(JComponent component) {
		if (component.getClass() == ProdTimerTable.class) {
			table = (ProdTimerTable) component;
		}
		if (isPaneEmpty()) {
			contents.add(new JScrollPane(component));
		} else {
			contents.remove(0);
			contents.add(new JScrollPane(component));
		}
	}

	public void pinTable(boolean isTablePinned) {
		table.pinElements(isTablePinned);
	}

	public static int getErrorMessage(String errorMessage) {
		JXLabel label = new JXLabel(errorMessage);
		label.setLineWrap(true);
		return JOptionPane.showOptionDialog(null,
				label,
				"Ошибка",
				JOptionPane.DEFAULT_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, null, null);
	}

	private boolean isPaneEmpty() {
		return contents.getComponents().length == 0;
	}

	@Override
	public void setJMenuBar(JMenuBar menubar) {
		menuBar = menubar;
		if (jMenuPane.getComponents().length == 0) {
			jMenuPane.add(menubar, 0);
		} else {
			jMenuPane.remove(0);
			jMenuPane.add(menubar);
		}
	}

	@Override
	public JMenuBar getJMenuBar() {
		return menuBar;
	}
}