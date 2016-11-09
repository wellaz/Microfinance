package soc.termination;

import java.awt.Font;
import java.io.File;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTable;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.Element;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;

import soc.helpers.OpenFile;
import soc.helpers.PDFHeaderFooter;

/**
 *
 * @author Wellington
 */
public class SubscriptionOnlyPDF {

	public SubscriptionOnlyPDF() {
	}

	public void generatePDF(String name, JLabel label, JTable table, String id, double grant, String description) {
		try {
			String filename = name + "_termination" + ".pdf";
			Document document = new Document(PageSize.A4, 40, 40, 40, 40);

			String path = System.getProperty("user.home") + File.separatorChar + filename;
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			Rectangle rect = new Rectangle(30, 30, 550, 800);
			writer.setBoxSize("art", rect);
			PDFHeaderFooter hp = new PDFHeaderFooter();
			writer.setPageEvent(hp);
			document.open();

			document.add(new Paragraph("Termination Form for " + name.toUpperCase() + " Account " + id,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("-----------------------------",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("Cashier Termination Module ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph(label.getText(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("________________________________________________________________________"));
			document.add(
					new Paragraph("Generated on " + (DateFormat.getDateInstance(DateFormat.LONG)).format(new Date()),
							FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("This Document is only issued by authorised signatory. Note: " + description,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, com.itextpdf.text.Font.ITALIC, BaseColor.RED)));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			PdfPTable ptable = new PdfPTable(table.getColumnCount());
			PdfPCell cell = new PdfPCell(new Paragraph("Termination Data",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, com.itextpdf.text.Font.ITALIC, BaseColor.BLUE)));
			cell.setColspan(table.getColumnCount());
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			ptable.addCell(cell);

			for (int i = 0; i < table.getColumnCount(); i++) {

				ptable.addCell(new Phrase(Header.header[i],
						FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
			}

			for (int rows = 0; rows < table.getRowCount(); rows++) {
				for (int cols = 0; cols < table.getColumnCount(); cols++) {
					ptable.addCell(table.getModel().getValueAt(rows, cols).toString());
				}
			}

			document.add(ptable);
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(
					"Temination Date :" + (DateFormat.getDateInstance(DateFormat.MEDIUM)).format(new Date())));
			document.add(new Paragraph(""));
			String grantString = (grant > 0) ? "" + grant : "None";
			document.add(new Paragraph("Amount To Be Received $" + grantString));
			document.add(new Paragraph(""));
			document.add(new Paragraph("Sign (Receiver) : __________________________Date_____________________"));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph("Sign (issuer) :   __________________________Date_____________________"));
			document.add(new Paragraph("................................................"));
			document.add(new Paragraph(".                                              ."));
			document.add(new Paragraph("                  Stamp                 ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, Font.ITALIC, BaseColor.LIGHT_GRAY)));
			document.add(new Paragraph(".                                              ."));
			document.add(new Paragraph(".                                              ."));
			document.add(new Paragraph("................................................"));
			document.add(new Paragraph(""));

			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph("                WITH THANKS!        "));
			document.close();

			new OpenFile().open(path);

		} catch (Exception ee) {
			JOptionPane.showMessageDialog(null, "File access Error\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}
	}
}
