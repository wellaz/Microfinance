/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.helpers;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.ColumnText;
import com.itextpdf.text.pdf.PdfPageEventHelper;
import com.itextpdf.text.pdf.PdfWriter;

/**
 *
 * @author Wellington
 */

public class PDFHeaderFooter extends PdfPageEventHelper {
	@Override
	public void onStartPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), rect.getLeft(),
				rect.getTop(), 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("Page " + document.getPageNumber(),
						FontFactory.getFont(FontFactory.TIMES_ITALIC, 5, Font.ITALIC, BaseColor.BLACK)),
				rect.getRight(), rect.getTop(), 0);
	}

	@Override
	public void onEndPage(PdfWriter writer, Document document) {
		Rectangle rect = writer.getBoxSize("art");
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER, new Phrase(""), rect.getLeft(),
				rect.getBottom(), 0);
		ColumnText.showTextAligned(writer.getDirectContent(), Element.ALIGN_CENTER,
				new Phrase("Copyright @"+new SetDateCreated().getYear()+" (Wellington Mapiku)",
						FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, Font.ITALIC, BaseColor.DARK_GRAY)),
				rect.getRight(), rect.getBottom(), 0);
	}
}
