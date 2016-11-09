/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package soc.ent;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import org.license.CheckKeys;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.embed.swing.JFXPanel;
import javafx.geometry.Pos;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.SVGPath;
import soc.helpers.AccessDbase;
import soc.helpers.IconImage;
import soc.helpers.LookAndFeelClass;
import soc.helpers.SetDateCreated;
import soc.loader.StartLoader;

/**
 *
 * @author Wellington
 */
@SuppressWarnings("serial")
public class FormValidation extends JFrame {
	@SuppressWarnings("unused")
	private static double xOffset = 0;
	@SuppressWarnings("unused")
	private static double yOffset = 0;
	@SuppressWarnings("unused")
	private final static String MY_PASS = "password1";
	private final static BooleanProperty GRANTED_ACCESS = new SimpleBooleanProperty(false);
	private final static int MAX_ATTEMPTS = 4;
	private final IntegerProperty ATTEMPTS = new SimpleIntegerProperty(0);
	TextField userName;
	PasswordField passwordField;
	AccessDbase adbase;
	Label note;
	private JLabel timelbl;

	public FormValidation() {
		super("L O G I N");
		this.setIconImage(new IconImage().createIconImage());
		new LookAndFeelClass().setLookAndFeels();
		this.setLayout(new BorderLayout());
		JPanel main = new JPanel(new FlowLayout());
		final JFXPanel fxPanel = new JFXPanel();
		Platform.runLater(() -> {
			init(fxPanel);
		});
		main.add(fxPanel, SwingConstants.CENTER);
		this.getContentPane().add(createTopPanel(), BorderLayout.NORTH);
		this.getContentPane().add(main, BorderLayout.CENTER);
		this.getContentPane().add(timePanel(), BorderLayout.SOUTH);
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

		Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
		this.setSize(600, 400);
		Dimension d = this.getSize();
		int x = (screen.width - d.width) / 2, y = (screen.height - d.height) / 2;
		this.setLocation(x, y);
		this.setResizable(false);

	}

	public void init(JFXPanel panel) {

		Scene scene = createScene();
		panel.setScene(scene);

	}

	private Scene createScene() {
		// Platform.runLater(() -> {
		Group root = new Group();

		// all text, borders, svg paths will use white
		javafx.scene.paint.Color foregroundColor = javafx.scene.paint.Color.rgb(255, 255, 255, .9);
		// rounded rectangular background
		Rectangle background = new Rectangle(320, 200);
		background.setX(0);
		background.setY(0);
		background.setArcHeight(15);
		background.setArcWidth(15);
		background.setFill(javafx.scene.paint.Color.rgb(0, 0, 0, .55));
		background.setStrokeWidth(1.5);
		background.setStroke(foregroundColor);
		// a read only field holding the user name.
		userName = new TextField();
		userName.setFont(javafx.scene.text.Font.font("SanSerif", 20));
		userName.setPromptText("Username");
		userName.setId("username");
		userName.textProperty().addListener((obs, ov, nv) -> {

		});
		// userName.textProperty().bind(user.userNameProperty());
		// wrap text node
		HBox userNameCell = new HBox();
		userNameCell.getChildren().add(userName);
		// pad lock
		SVGPath padLock = new SVGPath();
		padLock.setFill(foregroundColor);
		padLock.setContent(
				"M24.875,15.334v-4.876c0-4.894-3.981-8.875-8.875-8.875s-8.875,3.981-8.875,8.875v4.876H5.042v15.083h21.916V15.334H24.875zM10.625,10.458c0-2.964,2.411-5.375,5.375-5.375s5.375,2.411,5.375,5.375v4.876h-10.75V10.458zM18.272,26.956h-4.545l1.222-3.667c-0.782-0.389-1.324-1.188-1.324-2.119c0-1.312,1.063-2.375,2.375-2.375s2.375,1.062,2.375,2.375c0,0.932-0.542,1.73-1.324,2.119L18.272,26.956z");
		// first row
		HBox row1 = new HBox();
		row1.getChildren().addAll(userNameCell, padLock);
		// password text field
		passwordField = new PasswordField();
		passwordField.setFont(javafx.scene.text.Font.font("SanSerif", 20));
		passwordField.setPromptText("Password");
		passwordField.setId("password");
		// user.passwordProperty().bind(passwordField.textProperty());
		// error icon
		SVGPath deniedIcon = new SVGPath();
		deniedIcon.setFill(javafx.scene.paint.Color.rgb(255, 0, 0, .9));
		deniedIcon.setStroke(javafx.scene.paint.Color.WHITE);//
		deniedIcon.setContent(
				"M24.778,21.419 19.276,15.917 24.777,10.415 21.949,7.585 16.447,13.08710.945,7.585 8.117,10.415 13.618,15.917 8.116,21.419 10.946,24.248 16.447,18.746 21.948,24.248z");
		deniedIcon.setVisible(false);
		SVGPath grantedIcon = new SVGPath();
		grantedIcon.setFill(javafx.scene.paint.Color.rgb(0, 255, 0, .9));
		grantedIcon.setStroke(javafx.scene.paint.Color.WHITE);//
		grantedIcon.setContent("M2.379,14.729 5.208,11.899 12.958,19.648 25.877,6.733 28.707,9.561 12.958,25.308z");
		// grantedIcon.setVisible(false);
		StackPane accessIndicator = new StackPane();
		accessIndicator.getChildren().addAll(deniedIcon, grantedIcon);
		accessIndicator.setAlignment(Pos.CENTER_RIGHT);
		grantedIcon.visibleProperty().bind(GRANTED_ACCESS);
		// second row
		HBox row2 = new HBox(3);
		row2.getChildren().addAll(passwordField, accessIndicator);
		HBox.setHgrow(accessIndicator, Priority.ALWAYS);

		HBox noteLabel = new HBox();
		note = new Label();
		note.setTextFill(javafx.scene.paint.Color.RED);
		noteLabel.getChildren().add(note);

		HBox row3 = new HBox(10);
		Label loginlbl = new Label("Login");
		loginlbl.setId("loginlbl");
		Button login = new Button();
		login.setGraphic(loginlbl);
		login.setId("login");
		Tooltip logintip = new Tooltip();
		logintip.setId("logintip");
		logintip.setText("Login");
		login.setTooltip(logintip);
		Label cancellbl = new Label("Cancel");
		cancellbl.setId("cancellbl");
		Button cancel = new Button();
		cancel.setGraphic(cancellbl);
		cancel.setId("cancel");
		Tooltip canceltip = new Tooltip();
		canceltip.setText("Terminate the application");
		canceltip.setId("canceltip");
		cancel.setTooltip(canceltip);
		cancel.setOnAction(ctionEvent -> {
			SwingUtilities.invokeLater(() -> {
				System.exit(0);
			});

		});
		adbase = new AccessDbase();
		adbase.connectionDb();
		login.setOnAction((e) -> {
			try {
				String user = userName.getText();
				String pwd = passwordField.getText();
				String query = "SELECT * FROM users WHERE username='" + user + "'";
				adbase.rs = adbase.stm.executeQuery(query);
				if (adbase.rs.next()) {
					String dbuser = adbase.rs.getString(1);
					String dbpass = adbase.rs.getString(2);
					if (dbpass.equals(pwd)) {
						SwingUtilities.invokeLater(() -> {
							commonData();
						});

					} else {
						deniedIcon.setVisible(true);
						note.setText("Incorrect password for " + dbuser);
						passwordField.setText("");
					}
				} else {
					deniedIcon.setVisible(true);
					userName.setText("");
					passwordField.setText("");
					note.setText("Wrong Password ..retry...");
				}
				if (GRANTED_ACCESS.get()) {
					this.setVisible(false);
				} else {
					deniedIcon.setVisible(true);
				}
				ATTEMPTS.set(ATTEMPTS.add(1).get());
				// System.out.println("Attempts: " + ATTEMPTS.get());
			} catch (Exception eee) {
				eee.printStackTrace(System.err);
			}
		});

		row3.getChildren().addAll(login, cancel);

		// listener on number of attempts
		ATTEMPTS.addListener((obs, ov, nv) -> {
			if (MAX_ATTEMPTS == nv.intValue()) {
				// failed attemps
				SwingUtilities.invokeLater(() -> {
					java.awt.Toolkit.getDefaultToolkit().beep();
					JOptionPane.showMessageDialog(this,
							"Something might be wrong with your login credentials!\nSession Expired!");
					System.exit(0);
				});

			}
		});
		HBox topmost = new HBox();
		topmost.setId("topmost");

		// main container for evrything
		VBox formLayout = new VBox(4);
		formLayout.getChildren().addAll(topmost, row1, row2, noteLabel, row3);
		formLayout.setLayoutX(12);
		formLayout.setLayoutY(12);
		root.getChildren().addAll(background, formLayout);

		Scene scene = new Scene(root, javafx.scene.paint.Color.rgb(0, 0, 0, 0));
		scene.getStylesheets().add(new Renderer().getCSSFile());
		return (scene);

	}

	/*
	 * public void sessionData() { new CleanUp();
	 * LogData.details.add(userName.getText());
	 * LogData.details.add(passwordField.getText()); }
	 */

	public void commonData() {
		EventQueue.invokeLater(() -> {
			//this.setVisible(false);
			this.dispose();
			// sessionData();
			new StartLoader();
		});

	}

	public JPanel createTopPanel() {
		JPanel panel = new JPanel(new FlowLayout());
		panel.setBackground(new Color(0.5f, 0.5f, 1f));
		JLabel label = new JLabel("c a s h i e r !");
		label.setFont(new Font("Tahoma", Font.BOLD, 50));
		label.setForeground(Color.WHITE);
		panel.add(label, SwingConstants.CENTER);
		return panel;
	}

	public JPanel timePanel() {
		JPanel timerp = new JPanel(new BorderLayout());
		timerp.setBorder(null);
		timerp.setBackground(new Color(0.5f, 0.5f, 1f));
		timelbl = new JLabel();
		timelbl.setFont(new Font("Arial", Font.PLAIN, 30));
		timelbl.setForeground(Color.WHITE);
		timerp.add(timelbl, BorderLayout.CENTER);

		// start the clock
		Timer timer = new Timer(1000, new Listener());
		timer.start();

		return timerp;
	}

	// timer class
	class Listener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			EventQueue.invokeLater(() -> {
				Calendar now = Calendar.getInstance();
				int hr = now.get(Calendar.HOUR_OF_DAY);
				int min = now.get(Calendar.MINUTE);
				int sec = now.get(Calendar.SECOND);
				int AM_PM = now.get(Calendar.AM_PM);

				String day_night;
				if (AM_PM == 1) {
					day_night = "PM";
				} else {
					day_night = "AM";
				}

				timelbl.setText("TIME " + hr + ":" + min + ":" + sec + " " + day_night);
			});

		}
	}

	public static void main(String[] args) {
		/*
		 * EventQueue.invokeLater(() -> { new FormValidation().setVisible(true);
		 * });
		 */
		FormValidation fm = new FormValidation();
		fm.launcher();

	}

	public void launcher() {
		int month = new SetDateCreated().getMonth();
		String year = new SetDateCreated().getYear();
		CheckKeys ch = new CheckKeys();
		ch.check(Integer.toString(month), Integer.parseInt(year));

	}
}
