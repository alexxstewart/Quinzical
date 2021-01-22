package main;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Screen;
import javafx.stage.Stage;
import score.Score;
import score.ScoreBoard;
import style.AppStyle;

public class ScoreBoardScreen {
	
	private Stage _stage;
	private Scene _scene;
	private ScoreBoard _scores;
	
	public ScoreBoardScreen(Stage stage, Scene scene, ScoreBoard scores) {
		_stage = stage;
		_scene = scene;
		_scores = scores;
	}
	
	public void printBoard() {
		// border pane is the outer pane which has the title the scroll pane and the back button
		BorderPane pane = new BorderPane();
		
		// scroll pane is the inner pane which contains the Hbox
		ScrollPane scrollPane = new ScrollPane();
		AppStyle.setScrollPaneStyle(scrollPane);
		
		// create a VBox for nz high scores and one for international highScores
		VBox vbox1 = new VBox();
		VBox vbox2 = new VBox();
		vbox1.setAlignment(Pos.TOP_CENTER);
		vbox2.setAlignment(Pos.TOP_CENTER);
		vbox1.setPadding(new Insets(0,100,0,100));
		vbox2.setPadding(new Insets(0,100,0,100));
		
		// create headings for each score category
		Text nzText = new Text("New Zealand");
		Text internationalText = new Text("International");
		AppStyle.styleScoreText(nzText);
		AppStyle.styleScoreText(internationalText);
		vbox1.getChildren().add(nzText);
		vbox2.getChildren().add(internationalText);
		
		// iterate through the scores and add the details to each respective vbox
		for(int i = 0; i < _scores.getScoreListSize(); i++) {
			Score score = _scores.getScoreAtIndex(i);
			
			// if the score is for the nz module then add the score to the first vbox
			if(score.getWhichRegion()) {
				Text text = new Text(score.getName() + ":  " + score.getScore());
				AppStyle.styleNormalText(text);
				vbox1.getChildren().add(text);
			}else {
				// add the score to the second vbox
				Text text = new Text(score.getName() + ":  " + score.getScore());
				AppStyle.styleNormalText(text);
				vbox2.getChildren().add(text);
			}
		}
		
		
		// add both the vboxs to an hbox and add the hbox to the scrollPane
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(vbox1, vbox2);
		hbox.setAlignment(Pos.CENTER);
		scrollPane.setContent(hbox);
		
		// create title and back button elements
		Text titleText = new Text("Scoreboard");
		AppStyle.styleFinalTitle(titleText);
		
		HBox titleBox = new HBox();
		titleBox.setAlignment(Pos.CENTER);
		titleBox.getChildren().add(titleText);
		
		Button backButton = createBackButton();
		backButton.setMaxWidth(Double.MAX_VALUE);
				
		// add the elements to the border pane
		pane.setCenter(scrollPane);
		pane.setTop(titleBox);
		pane.setBottom(backButton);
		
		// create a new scene and set the scene of the stage to this scene
		Scene scene = new Scene(pane, 900, 700);
		
		AppStyle.setPaneStyle(pane);
		_stage.setScene(scene);
	}

	private Button createBackButton() {
		Button button = new Button("Back to Menu");
		AppStyle.styleButton(button);
	   	button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					_stage.setScene(_scene);
				}
		});
	   	return button;
	}
}
