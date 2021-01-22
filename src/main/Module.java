package main;

import Helpers.*;
import style.*;
import PopUps.*;
import FileEditing.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

import java.util.List;

/**
 * this is an abstract class which GamesModule and PracticeModule classes
 * extend. Common methods in each class is refactored in this class.
 * This class also implements the template method pattern because the behaviour of the code
 * is slightly different on each class.
 */
public abstract class Module {
    protected BorderPane _pane;

    protected Stage _stage;
    protected Scene _rootScene;
    protected List<Category> _categories;
    protected  TTS _tts;
    protected Slider _slider;
    protected VBox _vbox;
    protected Boolean _inGamesModule;
    protected Boolean _gameNeedsSaving = false;
    public String _region;
    
    private Timeline timeline;
    private int timerCount = 30;

    /**
     * this is main loop method which acts as a constructor of the class
     * @param stage
     * @param categories
     */
    protected void mainLoop(Stage stage, List<Category> categories){
        
    	_stage = stage;
    	_stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
    	      public void handle(WindowEvent we) {
    	    	  if(_inGamesModule) {
    	    		  // consume the exit event
    	    		  we.consume();
    	    		
    	    		  // create a new Stage for the popup window
    	    		  Stage newStage = new Stage();
    	    		  newStage.toFront();
    	    		  ExitPopUp popup = new ExitPopUp(_stage, categories, addWinnings(), _region);
    	    		  popup.start(newStage);
    	    	  }
    	      }
    	  });
        
        _categories = categories;
        _tts = new TTS();

        AppStyle.setPaneStyle(_pane);

        Scene scene = new Scene(_pane, 900, 700);

        scene.getStylesheets().add("style/style.css");

        _stage.setScene(scene);
        _stage.show();
    }

    /**
     * This method is invoked after the user has selected a question. It:
     *
     * - Finds and speaks the question
     * - Creates a text field for the users answer
     * - Creates buttons for submitting answer, user does not know answer, and a button which allows the user to play back the question
     *
     */
    protected void displayQuestionAndGetInput(Question question, Boolean isGamesModule){
        printBottomPane(isGamesModule);

        // the question has now been asked so we can update the answered field in the question object
        if(isGamesModule) {
        	question.setAnswered(true);
        }
        
        _vbox = new VBox();
        
        // get the question and speak it aloud
        String questionClue = question.getQuestion();
        _tts.destroyPreviousProcess();
        _tts.threadSafeSpeaking(questionClue);
        

        Text timerText;
        if(_inGamesModule) {
        	// create a text for the timer
        	timerText = new Text("Time left: ");
        	AppStyle.styleNormalText(timerText);
        	_vbox.getChildren().add(timerText);
        	
        	timerCount = 30;
        	
            // we can create a new thread to query the state of the process with out the program lagging
            new Thread() {
            	public void run() {
            		
            		Boolean state;
            		Boolean gotCancelled;
            		
            		_tts.setProcessCancelled(false);
            		_tts.setStateFalse();
            		
            		cancelTimer();
            		allowTimer();
            		
            		long startTime = System.currentTimeMillis();
                    while(true) {
                    	try {
                        	if(System.currentTimeMillis() - startTime > 500) {
                        		System.out.print("");
                        		startTime = System.currentTimeMillis();
                            	state = _tts.getProcessState();
                            	gotCancelled = _tts.getProcessCancelled();
                            	if(state || gotCancelled) {
                            		break;
                            	}
                        	}
                    	} catch (Throwable e) {
                    	   // this block will execute when anything "bad" happens
                    	}
                    }
                    // start the timer
                    if(!gotCancelled) {
                        startTimer(timerText, question);
                    }
                    _tts.setProcessCancelled(false);
    	        }
            }.start();
        }
        
        // add question label if necessary
        addQuestionLabel(_vbox, question);        
        
        // create a text area for the user to type their answer
        TextField field = new TextField();
        field.setAlignment(Pos.CENTER);
        field.setMaxWidth(300);

        // create three buttons for the user to submit the answer, don't know answer, and to the hear question again
        Button submitAnswerButton = createSubmitAnswerButton(field, question);
        Button dontKnowButton = createDontKnowButton(question);
        Button playBackButton = Media.createPlayBackButton(_tts, questionClue);

        // horizontally align the buttons
        HBox hbox = new HBox();
        hbox.getChildren().add(submitAnswerButton);
        
        if(dontKnowButton != null){
            hbox.getChildren().add(dontKnowButton);
            _gameNeedsSaving =  true;
        }
        
        hbox.getChildren().add(playBackButton);
        hbox.setAlignment(Pos.CENTER);
        hbox.setSpacing(10);

        _vbox.getChildren().addAll(field, hbox);
        _vbox.setAlignment(Pos.CENTER);
        _vbox.setSpacing(10);

        _pane.setCenter(_vbox);
    }

    /**
     * This method handles the printing of the winnings amount and the quit button at the bottom of the page
     * It has three elements:
     *
     * - Winnings count
     * - slider to control the speed of the TTS
     * - quit button so the user can quit whenever they want
     *
     */
    public void printBottomPane(boolean isGamesModule) {
        // create a hbox to store the winnings amount and the quit to main menu button
        HBox hbox = new HBox();
        //hbox.setSpacing(200);

        // create the slider
        _slider = Media.createSlider(_tts);
        AppStyle.styleSlider(_slider);
        
        //add text just above the slider with a vbox
        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);
        Text textAboveSlider = new Text("-   Voice Speed   +");
        AppStyle.styleSmallText(textAboveSlider);
        vbox.getChildren().addAll(textAboveSlider, _slider);
        
        // add padding to the vbox
        vbox.setPadding(new Insets(10,0,10,0));

        // display the current winnings
        Text winningsText;
        if(addWinnings() == null){
            winningsText = new Text("Practice Mode");
        }else {
            winningsText = new Text("Current Score: " + Integer.toString(addWinnings()));
        }

        // allow the user to quit the game at any time
        Button mainMenuButton = createQuitToMainMenuButton(isGamesModule, false);
        Button resetGameButton = createResetGameButton();

        // region allows us to have evenly spaced elements in the hbox
        Region region = new Region();
        Region region1 = new Region();
        HBox.setHgrow(region, Priority.ALWAYS);
        HBox.setHgrow(region1, Priority.ALWAYS);
        
        if(winningsText != null){
        	AppStyle.styleNormalText(winningsText);
            hbox.getChildren().addAll(winningsText, region);
        }
        
        //add the element to the box and pane
        if(isGamesModule) {
        	HBox buttonHBox = new HBox(10);
        	buttonHBox.getChildren().addAll(resetGameButton, mainMenuButton);
        	hbox.getChildren().addAll( vbox, region1, buttonHBox); 
        }else {
        	hbox.getChildren().addAll( vbox, region1, mainMenuButton);        
        }
       	_pane.setBottom(hbox);
    }
    
	/*
	 * ResetAll is called when the user wants to replay the game. It simply
	 * 
	 * - invokes the resetQuestions method
	 */
	public void resetAll() {
		WriteFile.removeSaves(_region);
		_gameNeedsSaving = false;
		for(Category category:_categories) {
			category.resetQuestions();
		}
	}

    /**
     * This method sets a root scene to return to title menu
     * @param scene
     */
    public void setRootScene(Scene scene){
        _rootScene = scene;
    }
    
    /*
     * This method sets the region for the module
     * e.g. whether its in international or nz region for questions
     */
    public void setRegion(String region) {
    	_region = region;
    }
    
    public String getRegion() {
    	return _region;
    }

    public Button createSubmitAnswerButton(TextField textField, Question question) {
        Button button = new Button("Submit");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                //answer screen
                answerScreen(textField, question);
                cancelTimer();
            }
        });
        return button;
    }

    public Button createReturnButton() {
        Button button = new Button("Return");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // we now want to go back to the question board
                printQuestionBoard();
            }
        });
        return button;
    }

    public Button createQuitToMainMenuButton(boolean inGamesModule, Boolean completeGame) {
        Button button = new Button("Main Menu");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	_inGamesModule = false;
                // we now want to go back to the main menu
                
            	// if we are in the games module then we always want to prompt the user to save the game
            	if(inGamesModule && !completeGame){
                    // ask user to save
            		MainMenuPopUp popup = new MainMenuPopUp(_categories, addWinnings(), _region);
            		
            		// create a new stage foe the popup and make it always stay at the front
            		Stage newStage = new Stage();
            		newStage.setAlwaysOnTop(true);
            		
            		// start the pop up
            		popup.start(newStage);
            		_stage.setScene(_rootScene);
            		cancelTimer();
            		
                }else {
                	// we are in the practice module so we just go back to the main menu
                	_stage.setScene(_rootScene);
                }
            }
        });
        return button;
    }

    /*
    hook methods. These are to be implemented by concrete sub classes

     */
    protected abstract void addQuestionLabel(VBox vbox, Question question);

    protected abstract Integer addWinnings();

    protected abstract void printQuestionBoard();

    protected abstract void setUp();

    protected abstract Button createDontKnowButton(Question question);
    
    protected abstract Button createResetGameButton();

    protected abstract void answerScreen(TextField field, Question question);
    
    protected abstract void startTimer(Text text, Question question);
    
    protected abstract void printTimer(Text text, Question question);
    
    protected abstract void cancelTimer();
    
    protected abstract void allowTimer();
}
