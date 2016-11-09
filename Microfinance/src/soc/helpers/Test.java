package soc.helpers;

import javafx.animation.TranslateTransition;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.embed.swing.JFXPanel;
import javafx.event.ActionEvent;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.util.Duration;

public class Test {

	public void init(JFXPanel panel) {
		Scene scene = createScene();
		panel.setScene(scene);
	}

	public Scene createScene() {
		Group tickerArea = new Group();
		FlowPane tickerContent = new FlowPane();

		//javafx.scene.paint.Color foregroundColor = javafx.scene.paint.Color.rgb(255, 255, 255, .9);
		

		// add some news
		Text news = new Text();
		news.setText(
				"Copyright © 2016. All Rights Reserved. Powered By Wellington Mapiku @ Data BootStrappers, Inc  mapikuw@dbs.co.zw, +263 77 553 4461");
		news.setFill(Color.WHITE);
		tickerContent.getChildren().add(news);
		DoubleProperty centerContentY = new SimpleDoubleProperty();
		tickerContent.translateYProperty().bind(centerContentY);
		HBox formLayout = new HBox(4);
		formLayout.getChildren().addAll(tickerContent);
		formLayout.setLayoutX(12);
		formLayout.setLayoutY(12);
		tickerArea.getChildren().add( formLayout);
		Scene scene = new Scene(tickerArea, 320, 112, Color.GREY);
		// scroll news feed
		TranslateTransition tickerScroller = new TranslateTransition();
		tickerScroller.setNode(tickerContent);
		tickerScroller.setDuration(Duration.millis(scene.getWidth() * 40));
		tickerScroller.fromXProperty().bind(scene.widthProperty());
		tickerScroller.toXProperty().bind(tickerContent.widthProperty().negate());
		// when ticker has finished, reset and replay ticker animation
		tickerScroller.setOnFinished((ActionEvent ae) -> {
			tickerScroller.stop();
			tickerScroller.setDuration(Duration.millis(scene.getWidth() * 40));
			tickerScroller.playFromStart();
		});
		// start ticker after nodes are shown
		tickerScroller.play();
		return scene;
	}

}
