package litheraa.view.table;

import com.github.weisj.darklaf.listener.PopupMenuAdapter;
import litheraa.ViewController;
import litheraa.data.ColumnDataTypeEnum;
import litheraa.view.DateChooseDialog;
import lombok.Getter;
import org.jdesktop.swingx.JXLabel;

import javax.swing.*;
import javax.swing.event.PopupMenuEvent;
import javax.swing.table.JTableHeader;
import java.awt.*;

@Getter
public class HeaderPopup extends JPopupMenu {

	private final Container panel = new Container();
	private final ProdTimerTable table;
	private int columnNo;
	private final FilterTextField filterField;

	public HeaderPopup(ProdTimerTable table) {
		this.table = table;
		setPopupSize(100, 54);

		JXLabel label = new JXLabel("Фильтр");
		label.setBorder(BorderFactory.createEmptyBorder(3, 3, 3, 3));
		label.setTextAlignment(JXLabel.TextAlignment.CENTER);
		add(label);

		filterField = new FilterTextField(table.getSorter(), this, true);

		SpringLayout layout = new SpringLayout();
		layout.putConstraint(SpringLayout.HORIZONTAL_CENTER, filterField, 0, SpringLayout.HORIZONTAL_CENTER, panel);
		layout.putConstraint(SpringLayout.NORTH, filterField, 0, SpringLayout.NORTH, panel);
		layout.putConstraint(SpringLayout.SOUTH, filterField, (int) filterField.getPreferredSize().getHeight(), SpringLayout.NORTH, panel);

		add(panel);
		panel.setLayout(layout);
		panel.setBounds(1, 1, 1, 1);

		DateChooseDialog dateChooseDialog = new DateChooseDialog();
		dateChooseDialog.addActionListener(dateChooseDialog.getFilterAction());
		dateChooseDialog.setVisible(false);
		dateChooseDialog.setInContainer(panel, filterField);

		panel.add(filterField);
		panel.add(dateChooseDialog);

		addPopupMenuListener(new PopupMenuAdapter() {
			@Override
			public void popupMenuWillBecomeVisible(PopupMenuEvent e) {
				columnNo = table.getActiveColumn();
				int filterStorageNo = table.convertColumnIndexToView(columnNo);
				changePopupLocation();
				if (table.getColumnType(columnNo) == ColumnDataTypeEnum.DATE) {
					setPopupSize(460, 240);
					dateChooseDialog.setColors(ViewController.getTheme());
					dateChooseDialog.setText(table.getFilterStorage(filterStorageNo));
					dateChooseDialog.setVisible(true);
					filterField.setFocusable(false);
				} else {
					dateChooseDialog.setVisible(false);
					filterField.setColumns(8);
					filterField.setFocusable(true);
					filterField.setText(table.getFilterStorage(filterStorageNo));
				}
				table.setFilter(true);
			}

			@Override
			public void popupMenuWillBecomeInvisible(PopupMenuEvent e) {
				setPopupSize(100, 54);
				table.setFilter(!filterField.getText().isEmpty());
			}
		});
	}

	private void changePopupLocation() {
		JTableHeader header = table.getTableHeader();
		for (int i = 0, coordinateX = 0; i < columnNo + 1; i++) {
			coordinateX += (int) header.getHeaderRect(i).getWidth();
			Point p = header.getLocationOnScreen();
			setLocation(coordinateX + (int) p.getX(), (int) p.getY());
		}
	}

	public void setFilterStorage(String value, int column) {
		table.setFilterStorage(value, column);
	}
}
