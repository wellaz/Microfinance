package soc.accounts.search;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
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
import javax.swing.JTextArea;
import javax.swing.SwingWorker;

import com.itextpdf.text.BaseColor;
import com.itextpdf.text.Document;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfWriter;

import soc.helpers.OpenFile;
import soc.helpers.PDFHeaderFooter;
import soc.helpers.PauseThread;

/**
 *
 * @author Wellington
 */
public class GenerateAccPDF {
	String tablename = "Report";

	ResultSet rs;
	Statement stm;
	String date, date1;
	JTextArea display;
	JLabel tellername;
	Document document;
	PdfWriter writer;
	Graphics2D g2d;

	public GenerateAccPDF(ResultSet rs, Statement stm, String date, String date1, JTextArea display,
			JLabel tellername) {
		this.rs = rs;
		this.stm = stm;
		this.date = date;
		this.date1 = date1;
		this.display = display;
		this.tellername = tellername;

	}

	public void generatePDF() {
		String labels = "Transactions\t\tDebit\t\tCredit\t\tBalance";
		try {
			String filename = tablename + ".pdf";
			document = new Document(PageSize.A4, 40, 40, 40, 40);

			String path = System.getProperty("user.home") + File.separatorChar + filename;
			writer = PdfWriter.getInstance(document, new FileOutputStream(path));
			Rectangle rect = new Rectangle(30, 30, 550, 800);
			writer.setBoxSize("art", rect);
			PDFHeaderFooter hp = new PDFHeaderFooter();
			writer.setPageEvent(hp);
			document.open();

			document.add(new Paragraph("** Transaction History Enquiry **",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("-----------------------------",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph(tellername.getText(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("Date Range " + date + " - " + date1,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("------------------------------",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("________________________________________________________________________"));
			document.add(new Paragraph("Generated on " + new Date().toString(),
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 9, Font.ITALIC, BaseColor.BLACK)));
			document.add(new Paragraph("This Document is only issued by authorised signatory",
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 7, com.itextpdf.text.Font.ITALIC, BaseColor.RED)));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));
			document.add(new Paragraph("  "));

			document.add(new Paragraph(labels,
					FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC, BaseColor.BLACK)));

			PdfContentByte cb = writer.getDirectContent();
			@SuppressWarnings("deprecation")
			Graphics2D g2d = cb.createGraphics(PageSize.A4.getWidth(), PageSize.A4.getHeight());
			display.printAll(g2d);
			g2d.dispose();

			/*
			 * document.add(new Paragraph(display.getText(),
			 * FontFactory.getFont(FontFactory.TIMES_ROMAN, 10, Font.ITALIC,
			 * BaseColor.BLACK)));
			 */

			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			document.add(new Paragraph(""));
			// itext data to represent the stamp
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

			new PauseThread().pause(20);
			new OpenFile().open(path);

		} catch (Exception ee) {
			ee.printStackTrace(System.err);
			JOptionPane.showMessageDialog(null, "File access Error\n" + ee.getMessage(), "Error",
					JOptionPane.ERROR_MESSAGE);
		} finally {
			writer.close();
			document.close();
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
			dialog.setVisible(false);
		}
	}
}
