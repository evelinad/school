package tables;

import java.util.Vector;

import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumnModel;
import javax.swing.table.TableModel;
/**
 * 
 * the class extends JTable, making each cell not editable
 *
 */
public class P2PJTable extends JTable {

	public P2PJTable() {
	}

	public boolean isCellEditable(int row, int column)
	{
		return false;
		
	}
	public P2PJTable(TableModel dm) {
		super(dm);
	}

	public P2PJTable(TableModel dm, TableColumnModel cm) {
		super(dm, cm);
	}

	public P2PJTable(int numRows, int numColumns) {
		super(numRows, numColumns);
	}

	public P2PJTable(Vector rowData, Vector columnNames) {
		super(rowData, columnNames);
	}

	public P2PJTable(Object[][] rowData, Object[] columnNames) {
		super(rowData, columnNames);
	}

	public P2PJTable(TableModel dm, TableColumnModel cm, ListSelectionModel sm) {
		super(dm, cm, sm);
	}
	
	
	@Override  
    public void changeSelection(int rowIndex, int columnIndex,  
            boolean toggle, boolean extend)
	{  
        super.changeSelection(rowIndex, columnIndex, true, false);  
    }  

}
