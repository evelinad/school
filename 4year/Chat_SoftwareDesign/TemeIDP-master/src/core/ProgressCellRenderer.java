package core;

import java.awt.Component;

import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * 
 * Class for updating a progress bar in a tranfsre table row
 *
 */
public class ProgressCellRenderer extends JProgressBar implements
		TableCellRenderer {

	private static final long serialVersionUID = -2294931500959570838L;

	public ProgressCellRenderer() {
		super(0,100);
		setValue(0);
		setString("0%");
		setStringPainted(true);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value,
			boolean isSelected, boolean hasFocus, int row, int column) {
		final String val = value.toString();
		int index = val.indexOf('%');
		if (index != -1)
		{
			int progress = 0;
			try {
				progress = Integer.parseInt(val.substring(0,index));
			} catch (NumberFormatException e) {
				e.printStackTrace();
			}
			setValue(progress);
			setString(val);
		}
		return this;
	}

	
}
