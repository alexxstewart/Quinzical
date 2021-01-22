package main;

import FileEditing.*;
import PopUps.*;
import Helpers.*;
import style.*;

import java.util.ArrayList;
import java.util.List;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import score.Score;
import score.ScoreBoard;
/**
 * This class creates the main menu of Quinzical.
 */
public class Quinzical {
	private BorderPane pane = new BorderPane();
	private List<Category> _categories;
	private List<String> _colors;
	
	private Stage _stage;
	private Scene _rootScene;
	private MainPage _mainPage;
	
	private String _region;
	private ScoreBoard _scores;

	// create new instances of the gamesModule and practiceModule
	private GamesModule gamesModule;
	private PracticeModule practiceModule;
	
	// default button dimensions
	private int _BUTTONWIDTH = 150;
	private int _BUTTONHEIGHT = 35;
	
	Quinzical(Stage stage, Scene rootScene, String region, ScoreBoard scores, MainPage mainPage){
		_stage = stage;
		_rootScene = rootScene;
		_region = region;
		_scores = scores;
		_mainPage = mainPage;
		
    	// read input files
    	if(ReadFile.ReadWinnings(_region) == -1) {
    		_categories = ReadFile.ReadCategoryFile(_region);
    	}else {
    		_categories = ReadFile.ReadSaveFile(_region);
    	}
    	
    	_colors = AppStyle.createColorList();
    	
    	// create new instances of the gamesModule and practiceModule
    	gamesModule = new GamesModule(_colors);
    	practiceModule = new PracticeModule(_colors);
	}
    
    public void printScene() {
    	pane = new BorderPane();
    	AppStyle.setPaneStyle(pane);
    	
    	TTS tts = new TTS();
    	tts.threadSafeSpeaking("Welcome to Quinzical");
    	
    	//check which region it is to display the right title
    	Text titleText;
    	Text triviaTitle = new Text("Trivia");
    	
    	if(_region.equals("nz")) {
    		titleText = new Text("New Zealand");
    		pane.setId("nzPane");
    	}else {
    		titleText = new Text("International");
    		pane.setId("internationalPane");
    	}
    	AppStyle.styleQBTitle(titleText, true);
    	AppStyle.styleQBTitle(triviaTitle, true);
    	
    	// create a vbox to store the titles
    	VBox titles = new VBox();
    	titles.setAlignment(Pos.CENTER);
    	titles.getChildren().addAll(titleText, triviaTitle);
    	
    	// create vbox to store nodes in vertical layout
    	VBox _vbox = new VBox();
    	_vbox.getChildren().addAll(createMainMenuButtons());
    	_vbox.setAlignment(Pos.CENTER);
    	_vbox.setSpacing(5);

    	VBox vbox = new VBox(50);
    	vbox.getChildren().addAll(titles, _vbox);
    	vbox.setAlignment(Pos.CENTER);
    	
    	// add the vbox to the center of the screen
    	pane.setCenter(vbox);
    	
		Scene scene = new Scene(pane,900,700, Color.WHITE);
		scene.getStylesheets().add("style/style.css");
		
		//set root scene
		practiceModule.setRootScene(scene);
		gamesModule.setRootScene(scene);
		
		// set region
		practiceModule.setRegion(_region);
		gamesModule.setRegion(_region);
		
		// set scores
		gamesModule.setScoreBoard(_scores);
		
		// set and show scene
		_stage.setScene(scene);
		_stage.show();
    }

    
	/**
	 * Creates all buttons needed for the main menu
	 * and return a list consisting of them.
	 *
	 * @return list of Button objects
	 */
	private List<Button> createMainMenuButtons(){
		List<Button> list = new ArrayList<>();

		//Add all three buttons to the list
		list.add(createPracticeModuleButton());
		list.add(createGamesModuleButton());
		list.add(createReturnToMainMenuButton());

		for(Button button : list){
			AppStyle.styleButton(button);
			button.setPrefWidth(_BUTTONWIDTH);
			button.setPrefHeight(_BUTTONHEIGHT);
		}

		return list;
	}

	/**
	 * Create button to go to practice module
	 * @return Button object that goes to practice module
	 */
	private Button createPracticeModuleButton() {
		Button button = new Button("Practice Questions");
		button.setId("button");
    	button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// call the practice module form here
				practiceModule.practiceLoop(_stage, _categories);
			}
		});
    	return button;
	}

	/**
	 * @return Button object that goes to games module
	 */
	private Button createGamesModuleButton() {
		Button button = new Button("Play Game");
		button.setId("button");
    	button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// call the Games module form here
				gamesModule.gamesLoop(_stage, _categories, _region, _mainPage, gamesModule);
			}
		});
    	return button;
	}
	
	private Button createReturnToMainMenuButton() {
		Button button = new Button("Main Menu");
		button.setId("button");
    	button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_mainPage.reprint();
				_stage.setScene(_rootScene);
			}
		});
    	return button;
	}
	
	public Boolean checkIf2QuetionsHaveBeenAsked() {
		int numQuestionsAsked = 0;
		for(Category category:_categories) {
			numQuestionsAsked += category.numQuestionsAsked();
		}
		if(numQuestionsAsked>=2) {
			return true;
		}
		return false;
	}
}
