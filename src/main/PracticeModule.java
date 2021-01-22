package main;

import style.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * This class handles the practicing functionality for the app
 * The method practice loop is invoked from the quinzical class 
 * 
 * THe method practiceLoop then invokes other helper methods which are tasked with different aspects of the scenes.
 */
public class PracticeModule extends Module{
	private GridPane _categoryPane;
	private int attemptsLeft;

	private List<String> _colors;
	
	PracticeModule(List<String> colors){
		_colors = colors;
	}
	
	
	/**
	 * main game loop. Invoked from quinzical class
	 */
	public void practiceLoop(Stage stage, List<Category> categories) {
		_inGamesModule = false;
		_pane = new BorderPane();
		mainLoop(stage, categories);
		
		// set the background image of pane
		// set the background image of the pane
    	if(_region.equals("nz")) {
    		_pane.setId("nzPane");
    	}else {
    		_pane.setId("internationalPane");
    	}

		setUp();
		printQuestionBoard();

	}

	/**
	 * This is a method invoked from the abstract class that prints question board.
	 */
	@Override
	protected void printQuestionBoard(){
		_categoryPane = new GridPane();
		
		// set spacing in and around elements
		_categoryPane.setHgap(20);
		_categoryPane.setVgap(20);
		_categoryPane.setPadding(new Insets(20, 20, 20, 20));
		
		_categoryPane.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
		int col = 0;
		int row = 0;

		List<ColumnConstraints> cols = new ArrayList<>();

		int colorCounter = 0;
		
		while(5*row + col < _categories.size()){
			while(col < 5 && 5*row + col < _categories.size()){
				Category category = _categories.get(5*row + col);
				Button button = new Button(category.getCategoryTitle());

				button.setId("practiceModuleButton");
				if(colorCounter >= _colors.size()) {
					colorCounter = 0;
				}
				
				AppStyle.styleBigButton(button, _colors.get(colorCounter));
				
				button.setOnAction(new EventHandler<ActionEvent>() {
					@Override
					public void handle(ActionEvent actionEvent) {
						attemptsLeft = 3;
						askQuestion(getRandomQuestion(category));
					}
				});

				button.setPrefHeight(1000);
				button.setMaxHeight(400/(_categories.size()/5 + 1));
				button.setPrefWidth(Double.MAX_VALUE);
				button.setMaxWidth(900/5);
				
				_categoryPane.add(button, col, row);

				col++;
				colorCounter++;
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
		Text text = new Text("Choose a category:");
		AppStyle.styleNormalText(text);

		// create the quizical title and add it to the top of the page
		Text titleText = new Text("Quinzical");
		AppStyle.styleQBTitle(titleText, false);
		
		// create an hbox for the title text
		HBox hbox = new HBox();
		hbox.getChildren().add(titleText);
		hbox.setPadding(new Insets(10,0,10,0));
		hbox.setAlignment(Pos.CENTER);

		// create a vbox and add both the title and the category text
		VBox vbox = new VBox();
		vbox.setSpacing(20);
		vbox.setAlignment(Pos.CENTER);
		vbox.getChildren().addAll(text, _categoryPane);
		
		_pane.setTop(hbox);
		_pane.setCenter(vbox);
		
		printBottomPane(false);
	}
	
	/**
	 * ask question gets called when the user initially clicks on a category and again after the user has clicked
	 */
	private void askQuestion(Question question) {
		displayQuestionAndGetInput(question, false);
	}

	/**
	 * This method chooses a question randomly given a category.
	 * @param category
	 * @return
	 */
	private Question getRandomQuestion(Category category){
		Random r = new Random();
		return category.getQuestion(r.nextInt(category.getQuestions().size()));
	}

	/**
	 * This method follows a template method pattern
	 * Question must be displayed in practice module, therefore, it has implementation
	 * in PracticeModule.
	 * @param vbox
	 * @param question
	 */
	@Override
	protected void addQuestionLabel(VBox vbox, Question question) {

		Text questionText = new Text(question.getQuestion());
		questionText.setWrappingWidth(800);
		questionText.setTextAlignment(TextAlignment.CENTER);
		AppStyle.styleNormalText(questionText);
		
		Text attemptsLeftText = new Text("Attempts left: " + Integer.toString(attemptsLeft));
		AppStyle.styleSmallText(attemptsLeftText);
		
		vbox.getChildren().addAll(questionText, attemptsLeftText);
	}
	
	protected Button createAnswerAgainButton(Question question) {
		Button button = new Button("Answer Again");
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	// we now want to go back to the question screen
            	askQuestion(question);
            }
        });
		return button;
	}

	/**
	 * This is one of the hook methods that are required by Module class
	 * It prints practice module version of answer screen
	 * @param field
	 * @param question
	 */
	@Override
	protected void answerScreen(TextField field, Question question) {
		
		attemptsLeft--;
		
		// first we validate the answer
		String userAnswer = field.getText();
		Boolean answerResult = question.validateAnswer(userAnswer);
		
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
			
		}else {
			// print and speak to the user
			Text text = new Text("Incorrect Answer!");
			Text text1 = new Text("The correct answer is " + question.getAnswer());
			
			_tts.threadSafeSpeaking("Incorrect Answer");
			
			AppStyle.styleNormalText(text);
			AppStyle.styleNormalText(text1);
			
			// create the two buttons
			Button returnButton =  createReturnButton();
			Button answerAgainButton = createAnswerAgainButton(question);

			// vbox holds the text
			VBox vboxText = new VBox();
			vboxText.setAlignment(Pos.CENTER);
			vboxText.setSpacing(10);
			
			// hbox holds the buttons
			HBox hbox = new HBox();
			hbox.setAlignment(Pos.CENTER);
			hbox.setSpacing(10);
			
			hbox.getChildren().add(returnButton);

			if(attemptsLeft == 0) {
				// here we only give the user the option to return to the categories page
				
				// we display the question then the answer to the question
				Text questionText = new Text("The Question is: " + question.getQuestion());
				Text answerText = new Text("The Answer was: " + question.getAnswer());
				
				// don't allow the question to spill over the edges of the page
				questionText.setWrappingWidth(800);
				questionText.setTextAlignment(TextAlignment.CENTER);
				
				answerText.setWrappingWidth(800);
				answerText.setTextAlignment(TextAlignment.CENTER);
				
				AppStyle.styleNormalText(questionText);
				AppStyle.styleNormalText(answerText);
				
				vboxText.getChildren().addAll(text, questionText, answerText);
			}else if(attemptsLeft == 1){
				// here we want to display the first letter of the answer
				Text clue = new Text("The first letter of the answer is: " + question.getAnswer().charAt(0));
				AppStyle.styleNormalText(clue);
				
				hbox.getChildren().add(answerAgainButton);
				
				vboxText.getChildren().addAll(text, clue);
			}else {
				// create both buttons
				vboxText.getChildren().add(text);
				
				hbox.getChildren().add(answerAgainButton);
			}
			
			vbox.getChildren().addAll(vboxText, hbox);
		}
		
		// then we build the screen around the result of the answer
		_pane.setCenter(vbox);
	
	}

	/**
	 * This is one of the hook methods required by Module class
	 * No reset game button needed so it returns null.
	 * @return
	 */
	protected Button createResetGameButton() {
		return null;
	}
	
	/**
	 * This method follows template method pattern
	 * No winning required to be displayed in bottom pane for practice module
	 * therefore, it returns null
	 * @return Integer
	 */
	@Override
	protected Integer addWinnings() {
		return null;
	}

	/**
	 * This method follows template method pattern
	 * No Dont know button needed so returns null
	 * @param question
	 * @return Button
	 */
	@Override
	protected Button createDontKnowButton(Question question) {
		return null;
	}
	
	//No set up for question required
	protected void setUp(){
	}

	@Override
	protected void printTimer(Text text, Question question) {
	}

	@Override
	protected void cancelTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void allowTimer() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected void startTimer(Text text, Question question) {
		// TODO Auto-generated method stub
		
	}
}
