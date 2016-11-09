package soc.deco;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 *
 * @author Wellington
 */
public class TrasparentTable {

	public static void transparentTable(JTable table) {
		table.setOpaque(false);
		((DefaultTableCellRenderer) table.getDefaultRenderer(Object.class)).setOpaque(false);
	}
}
