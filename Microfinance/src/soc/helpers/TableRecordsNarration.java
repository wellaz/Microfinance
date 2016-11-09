package soc.helpers;

import javax.swing.JTable;

/**
 *
 * @author Wellington
 */
public class TableRecordsNarration {

	public TableRecordsNarration() {

	}

	public String narrate(JTable table) {
		int rows = table.getRowCount();
		return (rows > 1) ? rows + " Records Found" : "1 Record Found";
	}
}
