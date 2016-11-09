package soc.deco;

import javax.swing.JScrollPane;

/**
 *
 * @author Wellington
 */
public class TransparentScrollPane {

	public static void transparentScrollPane(JScrollPane scroll) {
		scroll.setOpaque(false);
		scroll.getViewport().setOpaque(false);
	}
}
