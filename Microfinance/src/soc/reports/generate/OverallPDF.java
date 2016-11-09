package soc.reports.generate;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.FileOutputStream;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JTable;
import javax.swing.SwingWorker;

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

import soc.deco.AnimateDialog;
import soc.helpers.CommonPath;
import soc.helpers.OpenFile;
import soc.helpers.PDFHeaderFooter;
import soc.helpers.PauseThread;

/**
 *
 * @author Wellington
 */
public class OverallPDF {
	// String tablename = "Overal";
	String name, acc;

	JTable jtable1, jtable2, jtable4, jtable3;

	public OverallPDF(JTable jtable1, JTable jtable2, JTable jtable3, JTable jtable4, String name, String acc) {
		this.jtable1 = jtable1;
		this.jtable2 = jtable2;
		this.jtable3 = jtable3;
		this.jtable4 = jtable4;
		this.name = name;
		this.acc = acc;

	}

	public void generatePDF() {
		try {
			String filename = acc + (DateFormat.getDateInstance(DateFormat.MEDIUM)).format(new Date()) + ".pdf";
			
			Document document = new Document(PageSize.A4, 40, 40, 40, 40);

			String path = CommonPath.path() + filename;
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			Rectangle rect = new Rectangle(30, 30, 550, 800);
			writer.setBoxSize("art", rect);
			PDFHeaderFooter hp = new PDFHeaderFooter();
			writer.setPageEvent(hp);
			document.open();

			document.add(new Paragraph("Summary",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("-----------------------------",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("General Transaction History Enquiry For " + name.toUpperCase() + " Acc :" + acc,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("-------------------------------- ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("________________________________________________________________________"));
			document.add(new Paragraph("Generated on " + new Date().toString(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("This Document is only issued by authorised signatory",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, com.itextpdf.text.Font.ITALIC, BaseColor.RED)));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			PdfPTable table1 = new PdfPTable(jtable1.getColumnCount());
			PdfPCell cell1 = new PdfPCell(new Paragraph("subscriptions".toUpperCase(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, com.itextpdf.text.Font.ITALIC, BaseColor.BLUE)));
			cell1.setColspan(jtable1.getColumnCount());
			cell1.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell1.setHorizontalAlignment(Element.ALIGN_CENTER);
			table1.addCell(cell1);

			for (int i = 0; i < jtable1.getColumnCount(); i++) {

				table1.addCell(new Phrase(SubHeader.header[i],
						FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
			}

			for (int rows = 0; rows < jtable1.getRowCount(); rows++) {
				for (int cols = 0; cols < jtable1.getColumnCount(); cols++) {
					table1.addCell(jtable1.getModel().getValueAt(rows, cols).toString());
				}
			}

			PdfPTable table2 = new PdfPTable(jtable2.getColumnCount());
			PdfPCell cell2 = new PdfPCell(new Paragraph("commission earned".toUpperCase(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, com.itextpdf.text.Font.ITALIC, BaseColor.BLUE)));
			cell2.setColspan(jtable2.getColumnCount());
			cell2.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell2.setHorizontalAlignment(Element.ALIGN_CENTER);
			table2.addCell(cell2);

			for (int i = 0; i < jtable2.getColumnCount(); i++) {

				table2.addCell(new Phrase(CommHeader.header[i],
						FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
			}

			for (int rows = 0; rows < jtable2.getRowCount(); rows++) {
				for (int cols = 0; cols < jtable2.getColumnCount(); cols++) {
					table2.addCell(jtable2.getModel().getValueAt(rows, cols).toString());
				}
			}

			PdfPTable table3 = new PdfPTable(jtable3.getColumnCount());
			PdfPCell cell3 = new PdfPCell(new Paragraph("all debts".toUpperCase(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, com.itextpdf.text.Font.ITALIC, BaseColor.BLUE)));
			cell3.setColspan(jtable3.getColumnCount());
			cell3.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell3.setHorizontalAlignment(Element.ALIGN_CENTER);
			table3.addCell(cell3);

			for (int i = 0; i < jtable3.getColumnCount(); i++) {

				table3.addCell(new Phrase(DebtsHeader.header[i],
						FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
			}

			for (int rows = 0; rows < jtable3.getRowCount(); rows++) {
				for (int cols = 0; cols < jtable3.getColumnCount(); cols++) {
					table3.addCell(jtable3.getModel().getValueAt(rows, cols).toString());
				}
			}

			PdfPTable table4 = new PdfPTable(jtable4.getColumnCount());
			PdfPCell cell4 = new PdfPCell(new Paragraph("micro-reconciliation".toUpperCase(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, com.itextpdf.text.Font.ITALIC, BaseColor.BLUE)));
			cell4.setColspan(jtable4.getColumnCount());
			cell4.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell4.setHorizontalAlignment(Element.ALIGN_CENTER);
			table4.addCell(cell4);

			for (int i = 0; i < jtable4.getColumnCount(); i++) {

				table4.addCell(new Phrase(RecoHeader.header[i],
						FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
			}

			for (int rows = 0; rows < jtable4.getRowCount(); rows++) {
				for (int cols = 0; cols < jtable4.getColumnCount(); cols++) {
					table4.addCell(jtable4.getModel().getValueAt(rows, cols).toString());
				}
			}

			document.add(table1);
			document.add(new Paragraph(""));
			document.add(table2);
			document.add(new Paragraph(""));
			document.add(table3);
			document.add(new Paragraph(""));
			document.add(table4);
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph("Sign (issuer) : __________________________Date_____________________"));
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

			new PauseThread().pause(20);
			new OpenFile().open(path);

		} catch (Exception ee) {
			ee.printStackTrace(System.err);
			JOptionPane.showMessageDialog(null, "File access Error\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		}

	}

	public class Worker extends SwingWorker<Void, Void> {
		JDialog dialog;
		JProgressBar prog;
		JButton hider;
		JLabel waitlbl;
		JTable table;

		public Worker() {
			dialog = new JDialog();
			dialog.setLayout(new BorderLayout());
			prog = new JProgressBar();
			dialog.setUndecorated(true);
			hider = new JButton("Run in Background");
			hider.addActionListener((ActionEvent event) -> {
				new AnimateDialog().fadeOut(dialog, 100);
			});
			waitlbl = new JLabel("Processing....");
			dialog.addComponentListener(new ComponentAdapter() {
				@Override
				public void componentResized(ComponentEvent evvt) {
					dialog.setShape(new RoundRectangle2D.Double(0, 0, dialog.getWidth(), dialog.getHeight(), 5, 5));
				}
			});
			Box box = Box.createVerticalBox();
			box.add(waitlbl);
			box.add(prog);
			box.add(hider);
			dialog.getContentPane().setBackground(new Color(0.5f, 0.5f, 1f));
			dialog.getContentPane().add(box, BorderLayout.CENTER);
			dialog.setSize(300, 100);
			Dimension d = dialog.getSize(), screen = Toolkit.getDefaultToolkit().getScreenSize();
			int a = (screen.width - d.width) / 2, b = (screen.height - d.height) / 2;
			dialog.setLocation(a, b);
		}

		@Override
		protected Void doInBackground() throws Exception {
			prog.setIndeterminate(true);
			new AnimateDialog().fadeIn(dialog, 100);
			generatePDF();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			new AnimateDialog().fadeOut(dialog, 100);
		}
	}
}
