package main;

import java.util.concurrent.TimeUnit;

import FileEditing.ReadFile;
import Helpers.ToolTipHelp;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import score.Score;
import score.ScoreBoard;
import style.AppStyle;

public class MainPage extends Application{
	
	private BorderPane _pane;
	private Stage _stage;
	private Scene _scene;
	private ScoreBoard _scores;
	private Boolean _locked = true;
	private MainPage _mainPage;
	private Quinzical _internationalGame;
	private Quinzical _nzGame;
	
    public void start(Stage stage) throws Exception {
    	
    	_mainPage = this;
    	
    	_stage = stage;
    	
    	_stage.setTitle("Quinzical");
    	
    	_pane = new BorderPane();
    	AppStyle.setPaneStyle(_pane);
    	
    	_scene = new Scene(_pane,900,700, Color.WHITE);
    	_scene.getStylesheets().add("style/style.css");
		
		// create title and set style
    	Text titleText = new Text("Quinzical");
		AppStyle.styleQBTitle(titleText, true);
		
		// create a new scoreboard
		_scores = ReadFile.readScores();
		
		// create Buttons
		Button nzButton = createNZButton();
		Button internationalButton = createInternationalButton();
		Button quitButton = createExitButton();
		Button viewScoreBoardButton = createScoreBoardButton();
		
		// set the minimum width of all the buttons
		nzButton.setMinWidth(200);
		internationalButton.setMinWidth(200);
		quitButton.setMinWidth(200);
		viewScoreBoardButton.setMinWidth(200);
		
		// create a vbox to store all the elements
		VBox vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(titleText, nzButton, internationalButton, viewScoreBoardButton, quitButton);
		
		_pane.setCenter(vbox);
		
		// set and show scene
		_stage.setScene(_scene);
		_stage.show();
		
    	_internationalGame = new Quinzical(_stage, _scene, "international", _scores, _mainPage);
    	_nzGame = new Quinzical(_stage, _scene, "nz", _scores, _mainPage);
    	
    	// check if the international module can be unlocked
    	if(_nzGame.checkIf2QuetionsHaveBeenAsked()) {
    		unlock();
    		reprint();
    	}
    	
    }
    
    // launch application
	public static void main(String[] args) {
		launch(args);
	}
	
	public Button createNZButton() {
		Button button = new Button("New Zealand Trivia");
		button.setMinWidth(200);
		button.setId("button");
	   	button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// create an instance of quinzical for nz
					_nzGame.printScene();
				}
			});
		
		return button;
	}
	
	public Button createInternationalButton() {
		Button button;
		
		if(_locked) {
			button = new Button("International Trivia (Locked)");
			
			// create a new tooltip for the button
			Tooltip tooltip = new Tooltip("International section is unlocked once two \nquestions from New Zealand Trivia module \nare completed.");
			tooltip.setStyle("-fx-font-size: 14");
			//ToolTipHelp.hackTooltipStartTiming(tooltip);
			button.setTooltip(tooltip);
			
		}else {
			button = new Button("International Trivia");
		}
		
		button.setMinWidth(200);
		button.setId("button");
		
	   	button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					if(!_locked) {
						// create an instance of quinzical for nz
						_internationalGame.printScene();
					}
				}
			});
		
		return button;
	}
	
	public Button createExitButton() {
		Button button = new Button("Quit");
		button.setMinWidth(200);
		button.setId("button");
	   	button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// quit game
					Platform.exit();
					System.exit(0);
				}
		});
	   	
	   	return button;
	}
	
	public Button createScoreBoardButton() {
		Button button = new Button("View Scoreboard");
		button.setMinWidth(200);
		button.setId("button");
	   	button.setOnAction(new EventHandler<ActionEvent>() {
				@Override
				public void handle(ActionEvent event) {
					// add scores
					Score score1 = new Score("Alex", 900, true);
					Score score2 = new Score("Alex", 900, true);
					Score score3 = new Score("Alex", 96, true);
					Score score4 = new Score("Alex", 500, true);
					Score score5 = new Score("Alex", 900, true);
					Score score6 = new Score("Alex", 910, true);
					Score score7 = new Score("Alex", 900, true);
					Score score8 = new Score("Alex", 900, true);
					Score score9 = new Score("Alex", 900, true);
					Score score10 = new Score("Alex", 600, true);
					Score score11 = new Score("Alex", 700, false);
					Score score12 = new Score("Alex", 500, false);
					Score score13 = new Score("Alex", 900, false);
					Score score14 = new Score("Alex", 900, false);
					
					_scores.addScore(score1);
					_scores.addScore(score2);
					_scores.addScore(score3);
					_scores.addScore(score4);
					_scores.addScore(score5);
					_scores.addScore(score6);
					_scores.addScore(score7);
					_scores.addScore(score8);
					_scores.addScore(score9);
					_scores.addScore(score10);
					_scores.addScore(score11);
					_scores.addScore(score12);
					_scores.addScore(score13);
					_scores.addScore(score14);
					
					
					
					ScoreBoardScreen screen = new ScoreBoardScreen(_stage, _scene, _scores);
					screen.printBoard();
				}
		});
	   	
	   	return button;
	}
	
	public void unlock() {
		_locked = false;
	}
	
	public void lock() {
		_locked = true;
	}
	
	public void reprint() {
		// create title and set style
    	Text titleText = new Text("Quinzical");
		AppStyle.styleQBTitle(titleText, true);
		
		// create Buttons
		Button nzButton = createNZButton();
		Button internationalButton = createInternationalButton();
		Button quitButton = createExitButton();
		Button viewScoreBoardButton = createScoreBoardButton();
		
		// create a vbox to store all the elements
		VBox vbox = new VBox(5);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(titleText, nzButton, internationalButton, viewScoreBoardButton, quitButton);
		
		_pane.setCenter(vbox);
	}
	
}
