/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import java.awt.Color;

import javax.swing.text.DefaultHighlighter;
import javax.swing.text.Document;
import javax.swing.text.Highlighter;
import javax.swing.text.JTextComponent;

/**
 *
 * @author Wellington
 */
public class TextFinder {

    class Paint extends DefaultHighlighter.DefaultHighlightPainter {

        public Paint(Color c) {
            super(c);
        }

    }
    Highlighter.HighlightPainter painter = new Paint(Color.BLUE);

    public void refresh(JTextComponent comp) {
        Highlighter h = comp.getHighlighter();
        Highlighter.Highlight[] h1 = h.getHighlights();
        for (Highlighter.Highlight h11 : h1) {
            if (h11.getPainter() instanceof Paint) {
                h.removeHighlight(h11);
            }
        }

    }

    public void find(String match, JTextComponent comp) {
        refresh(comp);
        try {
            Highlighter h = comp.getHighlighter();
            Document doc = comp.getDocument();
            String txt = doc.getText(0, doc.getLength());
            int loc = 0;
            while ((loc = txt.toUpperCase().indexOf(match.toUpperCase(), loc)) >= 0) {
                h.addHighlight(loc, loc + match.length(), painter);
                loc += match.length();
            }

        } catch (Exception ee) {
            java.awt.Toolkit.getDefaultToolkit().beep();
        }
    }

}
