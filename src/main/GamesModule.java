package main;

import Helpers.*;
import style.*;
import PopUps.*;
import FileEditing.*;

import java.util.ArrayList;
import java.util.List;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import score.Score;
import score.ScoreBoard;

/**
 * This class handles the functionality for the game part of the app
 * 
 * The gamesLoop method is invoked from the Quinzical class.
 * The gamesLoop then invokes other helper methods to display the different scenes of the game
 */
public class GamesModule extends Module{
	private TilePane _tilePane = new TilePane();
	protected int _winnings;
	private int[] _randomCategoryIndexes;
	private String _region;
	private ScoreBoard _scores;
	private MainPage _mainPage;
	private int timerCount = 30;
	private Boolean stopTimer = false;
	private Timeline currentTimeline;
	private GamesModule _itself;
	
	private List<Category> assignedCategories;
	private List<String> _colors;
	
	GamesModule(List<String> colors){
		_colors = colors;
	}
	
	/**
	 * This is the main game module loop. Invoked from Quinzical main menu
	 * @param stage
	 * @param categories
	 */
	public void gamesLoop(Stage stage, List<Category> categories, String region, MainPage mainPage, GamesModule itself) {
		_region = region;
		_mainPage = mainPage;
		_inGamesModule = true;
		_itself = itself;
		_pane = new BorderPane();
		mainLoop(stage, categories);
		_winnings = 0;

		// we first need to check if there is a saved game so we can give the user the option to load game or play new game
		int winnings = ReadFile.ReadWinnings(_region);
		
		// if winnings == -1 then we have no saved data so we can continue as normal
		if(winnings == -1) {
			// setupGame is called to create prepare the categories and questions
			setUp();
		}else {
			// we have saved game data
			_winnings = winnings;
			newOrOldGame();
		}
		
		// set the background image of the pane
    	if(_region.equals("nz")) {
    		_pane.setId("nzPane");
    	}else {
    		_pane.setId("internationalPane");
    	}

	}
	
	/**
	 * This method is called at the start of the game in order to choose the random categories and the random questions for these categories
	 * 
	 */
	public void setUp() {
		// here we need to prompt the user to select 5 new categories
		
		_pane.getChildren().clear();
		
		assignedCategories = new ArrayList<Category>();
		
		GridPane _categoryPane = new GridPane();
		_categoryPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		
		// set spacing in and around elements
		_categoryPane.setHgap(20);
		_categoryPane.setVgap(20);
		_categoryPane.setPadding(new Insets(20, 20, 20, 20));
		
		int col = 0;
		int row = 0;
		
		int colorCounter = 0;

		List<ColumnConstraints> cols = new ArrayList<>();

		while(5*row + col < _categories.size()){
			while(col < 5 && 5*row + col < _categories.size()){
				Category category = _categories.get(5*row + col);
				Button button = new Button(category.getCategoryTitle());
				
				button.setPrefHeight(1000);
				button.setMaxHeight(400/(_categories.size()/5 + 1));
				button.setPrefWidth(Double.MAX_VALUE);
				button.setMaxWidth(900/5);
				GridPane.setFillWidth(button, true);
				
				if(colorCounter >= _colors.size()) {
					colorCounter = 0;
				}
				
				String currentColor = _colors.get(colorCounter);
				
				AppStyle.styleBigButton(button, currentColor);
				
				button.setId("buttonScreen");
				
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent event) {
						
						// if there are already 5 categories then we do not allow then to select more
						if(assignedCategories.size() != 5) {
							
							// if the button has not been selected previously and now has been selected then we can make it green
							if(assignedCategories.indexOf(category) == -1) {
								
								button.setId("clickedCategoryButton");
								assignedCategories.add(category);
							}else {
								/*
								 * The category has already been selected so now we want to remove it from
								 * the list and revert its color back to grey
								 */
								button.setId("onSecondClick");
								AppStyle.styleBigButton(button, currentColor);
								assignedCategories.remove(category);
							}
						}else {
							// their only other option is to deselect buttons
							button.setId("onSecondClick");
							AppStyle.styleBigButton(button, currentColor);
							assignedCategories.remove(category);
						}
						
						// we want to change the color of the button to a green
					}
				});
				colorCounter++;

				_categoryPane.add(button, col, row);

				col++;
			}
			row++;
			col = 0;

			ColumnConstraints co = new ColumnConstraints();
			co.setHgrow(Priority.ALWAYS);
			co.setFillWidth(true);
			co.setPercentWidth(20);
			cols.add(co);
		}

		_categoryPane.getColumnConstraints().addAll(cols);
		//_pane.setCenter(_categoryPane);

		// create text to display above categories
		Text text = new Text("Choose 5 categories below:");
		AppStyle.styleNormalText(text);

		// create a vbox and add both the title and the category text
		VBox vbox = new VBox();
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(text, _categoryPane);
		
		_pane.setCenter(vbox);
		
		Button continueButton = createContinueButton(vbox);
		_pane.setBottom(continueButton);
				
		// if we can continue then we can now print the both the bottom and the question board
		//printBottomPane(true);
		//printQuestionBoard();
	}
	
	private void assignQuestions() {
		// we have our assigned categories now we just need to assign questions
		for(Category category:assignedCategories) {
			try {
				category.assignFiveRandomQuestions();
			} catch (IllegalArgumentException e){
				Stage newStage = new Stage();
				newStage.toFront();
				ErrorPopUp popup = new ErrorPopUp(_stage, newStage, _region);
				popup.start(newStage);
			}
		}
	}

	/**
	 * This method sets up a game from previous game save files.
	 */
	private void setUpOldGame() {
		// initialise the current category list
		assignedCategories = new ArrayList<Category>();
		
		// we need to iterate through the categories and find the categories that were last being asked
		for(Category category:_categories) {
			if(category.reload()) {
				assignedCategories.add(category);
			}
		}
	}
	
	/*
	 * This method is called from the quinzical class to set the scores of the game.
	 */
	public void setScoreBoard(ScoreBoard scores) {
		_scores = scores;
	}
	
	/**
	 * This method creates a pane and sets it so that the question board
	 * is visible.
	 */
	@Override
	protected void printQuestionBoard() {
		
		printBottomPane(true);

		_tilePane.getChildren().clear();
		_tilePane.setHgap(20);
		_tilePane.setVgap(20);
		
		// get the 5 new colors to use
		List<String> newColors = AppStyle.lighterColors();
		int newColorsIterator = 0;
		
		// we also need to check that when no buttons are printed then the user should be shown a reward screen
		Boolean questionsLeft = false;
		for(Category category:assignedCategories) {
			// get the category we are showing and create a Text element for its title
			Text categoryTitleText = new Text(category.getCategoryTitle());
			
			AppStyle.styleCategoryTitle(categoryTitleText);
			
			// the vbox aligns the title and question values vertically
			VBox vbox = new VBox();
			vbox.setStyle("-fx-border-color: #aeadff;");
			vbox.setSpacing(5);
			vbox.getChildren().add(categoryTitleText);

			// retrieve the 5 questions for the category
			int[] questionIndexes = category.getCurrentQuestionIndexes();
			
			/* if we start with the lowest point value at the top we can check if it has been answered. If so we dont create a button and we move on to the next 
			 * question and so on...
			*/
			Boolean buttonMade = false;
			for(int questionIndex:questionIndexes) {
				
				// get the question in the index and get its respective value
				Question question = category.getQuestion(questionIndex);
				int questionValue = question.getQuestionValue();
				
				if(question.getAnswered() || buttonMade) {
					// we move onto the next question because this one has already been answered
					
					Text questionValueText = new Text(Integer.toString(questionValue));
					AppStyle.textColorWhite(questionValueText);
					
					if(question.gotRight()) {
						// we style the text a light green
						HBox rightHbox = new HBox();
						rightHbox.setBackground(new Background(new BackgroundFill(Color.web("#1f9900"),null,null)));
						rightHbox.getChildren().add(questionValueText);
						rightHbox.setAlignment(Pos.CENTER);
						vbox.getChildren().add(rightHbox);
						
						//AppStyle.textColorGreen(questionValueText);
					}else if(question.gotWrong()) {
						
						HBox rightHbox = new HBox();
						rightHbox.setBackground(new Background(new BackgroundFill(Color.web("#d62700"),null,null)));
						rightHbox.getChildren().add(questionValueText);
						rightHbox.setAlignment(Pos.CENTER);
						vbox.getChildren().add(rightHbox);
					}else {
						// we style the color a light grey
						AppStyle.textColorGrey(questionValueText);
						vbox.getChildren().add(questionValueText);
					}
				}else {
					// we now make the button and style it
					Button questionButton = createQuestionButton(question);
					
					questionButton.setId("questionButton");
					
					questionButton.setMaxWidth(Double.MAX_VALUE);
					VBox.setVgrow(questionButton, Priority.ALWAYS);
					
					vbox.getChildren().add(questionButton);
					
					buttonMade = true;
					questionsLeft = true;
				}
			}
			
			// set the padding of each vbox and align it with the center
			vbox.setPadding(new Insets(10,10,10,10));
			vbox.setAlignment(Pos.CENTER);
			_tilePane.getChildren().add(vbox);
			_tilePane.setAlignment(Pos.CENTER);
			
			newColorsIterator++;
		}
		// add the tilepane to the center of the main pane
		_pane.setCenter(_tilePane);
		
		// create a title element and style it
		Text title = new Text("Quinzical");
		AppStyle.styleQBTitle(title, false);
		
		// create a vbox to put the title inside which can then be centered and padded
		VBox vbox = new VBox();
		vbox.getChildren().add(title);
		vbox.setAlignment(Pos.CENTER);
		vbox.setPadding(new Insets(20,0,0,0));
		_pane.setTop(vbox);
		
		// if there are no more questions left in the game to answer then the reward screen is displayed
		if(!questionsLeft) {
			printRewardScreen();
		}
	}
	
	/**
	 * This method is invoked after the user has input their answer and clicked the submit button. It:
	 * 
	 * - Validates the users answer
	 * - If the answer is correct it reads a "Correct Answer" message, and updates the winnings
	 * - If the answer is incorrect it reads "Incorrect answer" to the user and gives them the correct answer
	 * - It then allows the user to return to the questions page
	 * 
	 */
	public void answerScreen(TextField field, Question question) {

		cancelTimer();
		
		// first we validate the answer
		String userAnswer = field.getText();
		Boolean answerResult = question.validateAnswer(userAnswer);
		
		// set the state of the question
		if(answerResult) {
			question.setRight();
		}else {
			question.setWrong();
		}
		
		//create a vbox to vertically align all the elements
		VBox vbox = new VBox();
		vbox.setSpacing(10);
		vbox.setAlignment(Pos.CENTER);
		if(answerResult) {
			// print and speak correct answer to the user
			Text text = new Text("Correct Answer!");
			_tts.threadSafeSpeaking("Correct Answer");
			AppStyle.styleNormalText(text);
			
			//create a return button and add the text and button to the page
			Button returnButton =  createReturnButton();
			vbox.getChildren().addAll(text, returnButton);
			
			// increase winnings by the question value amount
			_winnings += question.getQuestionValue();
		}else {
			// print and speak to the user
			Text text = new Text("Incorrect Answer!");
			Text text1 = new Text("The correct answer is: " + question.getAnswer());
			
			AppStyle.styleNormalText(text);
			AppStyle.styleNormalText(text1);

			_tts.threadSafeSpeaking("Incorrect Answer, the correct answer is " + question.getAnswer());
			
			// create the return button and add elements to the screen
			Button returnButton =  createReturnButton();
			vbox.getChildren().addAll(text,text1,returnButton);
		}
		
		// then we build the screen around the result of the answer
		_pane.setCenter(vbox);
		
		// update the score at the bottom of the screen
		printBottomPane(true);
	}

	protected Integer addWinnings(){
		return _winnings;
	}

	/**
	 * This method handles the situation when the user has completed all questions in the game. It:
	 * 
	 * - Prints and speaks to the screen a congratulations message
	 * - Allows the user to choose between playing again and quiting to the main menu
	 */
	public void printRewardScreen() {
		_pane.getChildren().clear();
		
		// print and speak the message to the user
		String message = "Congratulations!";
		String saveScoreMessage = "Do you want to save your score to the scoreboard?";
		Text titleText = new Text(message);
		Text scoreText = new Text("Your score was: " + Integer.toString(_winnings));
		Text saveScoreText = new Text(saveScoreMessage);
		_tts.threadSafeSpeaking(message + ", your score was " + Integer.toString(_winnings) + saveScoreMessage);
		
		// style the text elements
		AppStyle.styleFinalTitle(titleText);
		AppStyle.styleScoreText(scoreText);
		AppStyle.styleSaveScoreText(saveScoreText);
		
		// create a text Field so the user can enter their name to save
		TextField nameBox = new TextField("Enter your name here");
		nameBox.setAlignment(Pos.CENTER);
		
		nameBox.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
            	nameBox.clear();    
            }
		});
		
		// create save score button
		Button saveScoreButton = createSaveScoreButton(nameBox, _winnings);
		
		// place the text field for the name and the save score button into an hbox
		HBox saveScore = new HBox();
		saveScore.setAlignment(Pos.CENTER);
		saveScore.getChildren().addAll(nameBox, saveScoreButton);
		
		// create the two buttons for the users
		Button playAgainButton = createPlayAgainButton();
		Button quitButton = createQuitToMainMenuButton(true, true);
		
		// align the buttons horizontally
		HBox hbox = new HBox();
		hbox.setSpacing(10);
		hbox.getChildren().addAll(playAgainButton, quitButton);
		hbox.setAlignment(Pos.CENTER);
		
		// align all the elements vertically
		VBox vbox = new VBox();
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(titleText, scoreText, saveScoreText, saveScore, hbox);
		_pane.setCenter(vbox);
		
		// reset the winnings to zero and reset all the questions
		_winnings = 0;
		resetAll();
	}
	
	
	/*
	 * this method is called when the there is a saved game
	 * the user gets the choice whether they want to load the saved game or to reset and start a new game
	 */
	public void newOrOldGame() {
		
		// create the text
		Text text = new Text("Would you like to continue with your previous game or start a new game?");
		AppStyle.styleNormalText(text);
		
		// create a hbox for the buttons
		HBox hbox = new HBox(10);
		hbox.getChildren().addAll(createNewGameButton(), createOldGameButton());
		hbox.setAlignment(Pos.CENTER);
		
		// create a vbox and add buttons and text to it
		VBox vbox = new VBox(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(text, hbox);
		
		// set it to the center of the pane
		_pane.setCenter(vbox);
	}
	
	private void checkInternationalUnlockPopUp() {
		// check how many questions have been asked
		int numberOfQuestionsAsked = 0;
		for(Category category:assignedCategories) {
			numberOfQuestionsAsked += category.numQuestionsAsked();
		}
		
		// if 2 questions have been asked then unlock the international module
		if(numberOfQuestionsAsked == 2 && _region.contentEquals("nz")) {		
    		// create a new stage foe the popup and make it always stay at the front
    		Stage newStage = new Stage();
    		newStage.setAlwaysOnTop(true);
    		
			// create a popup nofifying the user that the international module has been unlocked
			UnlockNotificationPopUp popup = new UnlockNotificationPopUp(newStage);
    		
    		// start the pop up
    		popup.start(newStage);
			
			_mainPage.unlock();
		}
	}
	
	
	protected void startTimer(Text text, Question question) {
    	
		timerCount = 30;
		// start timer
        currentTimeline = new Timeline(new KeyFrame(
                Duration.millis(1000),
                ae -> printTimer(text, question)));
        currentTimeline.setCycleCount(31);
        currentTimeline.play();
    }
	
    public void printTimer(Text text, Question question) {
    	if(!stopTimer) {
        	text.setText("Time left: " + Integer.toString(timerCount--));
        	if(timerCount == -1) {
        		printRanOutOfTimeScreen(question);
        	}
    	}else {
    		currentTimeline.stop();
    		currentTimeline.getKeyFrames().clear();
    	}
    }
    
    protected void cancelTimer() {
    	stopTimer = true;
    }
    
    protected void allowTimer() {
    	stopTimer = false;
    }
	
    private void printRanOutOfTimeScreen(Question question) {
    	
    	question.setWrong();
    	
    	_pane.setCenter(null);
    	_pane.setBottom(null);
    	
    	Text text = new Text("You ran out of time");
    	AppStyle.styleFinalTitle(text);
    	Text text1 = new Text("The correct answer was: " + question.getAnswer());
    	AppStyle.styleNormalText(text1);
    	
    	Button returnButton = createReturnButton();
    	
    	VBox vbox = new VBox(10);
    	vbox.setAlignment(Pos.CENTER);
    	vbox.getChildren().addAll(text, text1, returnButton);
    	_pane.setCenter(vbox);
    }
    
    public void resetGame() {
		// Now we just want to go back to the question board and reset the game
    	resetAll();
		setUp();
		_winnings = 0;
    }

	
	/* ======================================================================================
	 * 
	 * All methods below are button creator methods
	 *
	 */
	
	public Button createQuestionButton(Question question) {
		Button button = new Button(Integer.toString(question.getQuestionValue()));
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// we now want to display the question and get the users input
				displayQuestionAndGetInput(question, true);
			}
		});
		return button;
	}
	
	public Button createSubmitAnswerButton(TextField textField,Question question) {
		Button button = new Button("Submit");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				answerScreen(textField, question);
			}
		});
		return button;
	}

	@Override
	protected void addQuestionLabel(VBox vbox, Question question) {
		//None because question is not needed to be printed.
	}
	
	public Button createResetGameButton() {
		Button button = new Button("Reset Game");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(_region.equals("nz")) {
					// we need to ask the user if they're sure they want to reset
					
		    		Stage newStage = new Stage();
		    		newStage.setAlwaysOnTop(true);
		    		
					// show the popup so the user can decide if they want to still reset
					ResetGamePopUp popup = new ResetGamePopUp(_itself, newStage, _mainPage);
		    		
		    		// start the pop up
		    		popup.start(newStage);
				}else {
					// we can just reset
					resetGame();
				}
			}
		});
		return button;
	}

	public Button createDontKnowButton(Question question) {
		Button button = new Button("Don't Know?");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Now we just want to go back to the question board
				question.setWrong();
				checkInternationalUnlockPopUp();
				printQuestionBoard();
				cancelTimer();
				
			}
		});
		return button;
	}
	
	public Button createNewGameButton() {
		Button button = new Button("New Game");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Now we just want to go back to the question board
				WriteFile.removeSaves(_region);
				resetGame();
			}
		});
		return button;
	}
	
	public Button createOldGameButton() {
		Button button = new Button("Load Game");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// Now we just want to go back to the question board
				setUpOldGame();
				printQuestionBoard();
			}
		});
		return button;
	}
	
	public Button createSaveScoreButton(TextField field, int score) {
		Button button = new Button("Save Score");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				// get the value of the name in the box
				String name = field.getText();
				Score newScore;
				if(_region.equals("nz")) {
					// create an instance of an nz score
					newScore = new Score(name, score, true);
				}else {
					// create an instance of an international score
					newScore = new Score(name, score, false);
				}
				_scores.addScore(newScore);
				WriteFile.writeHighScores(_scores);
			}
		});
		return button;
	}
	
	public Button createContinueButton(VBox vbox) {

		Button button = new Button("Continue");
		button.setMaxWidth(Double.MAX_VALUE);
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if(assignedCategories.size() != 5) {
					// we need to create a pop up to ask the user to input 5 categories
					Text text = new Text("Please choose 5 categories");
					AppStyle.styleReminderText(text);
					
					vbox.getChildren().add(text);
					_pane.setCenter(vbox);
				}else {
					// we can now assign questions and then print the question board
					_pane.getChildren().clear();
					assignQuestions();
					printBottomPane(true);
					printQuestionBoard();
				}
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
				checkInternationalUnlockPopUp();
				printQuestionBoard();
			}
		});
		return button;
	}
	
    public Button createPlayAgainButton() {
        Button button = new Button("Play Again");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // we now want to go back to the start of the game
                setUp();
                printQuestionBoard();
            }
        });
        return button;
    }
}
