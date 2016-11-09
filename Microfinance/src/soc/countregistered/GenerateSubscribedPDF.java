package soc.countregistered;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.RoundRectangle2D;
import java.io.File;
import java.io.FileOutputStream;
import java.sql.ResultSet;
import java.sql.Statement;
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

import soc.helpers.OpenFile;
import soc.helpers.PDFHeaderFooter;
import soc.helpers.PauseThread;

public class GenerateSubscribedPDF {
	String tablename = "Report";

	ResultSet rs;
	Statement stm;
	JTable jtable;

	public GenerateSubscribedPDF(ResultSet rs, Statement stm, JTable jtable) {
		this.rs = rs;
		this.stm = stm;
		this.jtable = jtable;

	}

	public void generatePDF() {
		try {
			String filename = tablename + ".pdf";
			Document document = new Document(PageSize.A4, 40, 40, 40, 40);

			String path = System.getProperty("user.home") + File.separatorChar + filename;
			PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			Rectangle rect = new Rectangle(30, 30, 550, 800);
			writer.setBoxSize("art", rect);
			PDFHeaderFooter hp = new PDFHeaderFooter();
			writer.setPageEvent(hp);
			document.open();

			document.add(new Paragraph("Report Generated",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("-----------------------------",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("Tell : ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("Email : ",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("________________________________________________________________________"));
			document.add(new Paragraph("Generated on " + new Date().toString(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("This Document is only issued by authorised signatory",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, com.itextpdf.text.Font.ITALIC, BaseColor.RED)));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			PdfPTable table = new PdfPTable(jtable.getColumnCount());
			PdfPCell cell = new PdfPCell(new Paragraph(tablename.toUpperCase(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 15, com.itextpdf.text.Font.ITALIC, BaseColor.BLUE)));
			cell.setColspan(jtable.getColumnCount());
			cell.setBackgroundColor(BaseColor.LIGHT_GRAY);
			cell.setHorizontalAlignment(Element.ALIGN_CENTER);
			table.addCell(cell);

			for (int i = 0; i < jtable.getColumnCount(); i++) {

				table.addCell(new Phrase(TableHeader1.header[i],
						FontFactory.getFont(FontFactory.TIMES_BOLD, 12, Font.BOLD, BaseColor.BLACK)));
			}

			for (int rows = 0; rows < jtable.getRowCount(); rows++) {
				for (int cols = 0; cols < jtable.getColumnCount(); cols++) {
					table.addCell(jtable.getModel().getValueAt(rows, cols).toString());
				}
			}

			document.add(table);
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
				dialog.setVisible(false);
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
			dialog.setVisible(true);
			generatePDF();
			return null;
		}

		@Override
		public void done() {
			prog.setIndeterminate(false);
			dialog.dispose();
		}

	}

}
