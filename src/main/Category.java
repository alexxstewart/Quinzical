package main;

import Helpers.*;
import java.util.ArrayList;
import java.util.List;

public class Category {
    private List<Question> _questions;
    private String _title;
    private int[] _currentQuestionIndexes = new int[5];
    
    public Category(String title){
    	_title = title;
    	_questions = new ArrayList<Question>();
    }

    /**
     * This method returns a list of all question objects
     * @return a list of question objects
     */
    public List<Question> getQuestions(){
        return _questions;
    }
    
    /*
     * This method returns a single question from an index position
     */
    public Question getQuestion(int index) {
    	return _questions.get(index);
    }
    
    /*
     * Method allows for adding question to the category 
     */
    public void addQuestion(Question question) {
    	_questions.add(question);
    }
    
    /**
     * This Method gets five random indexes between 0 and the number of questions in the category and assigns these values to 
     * a int array
     * 
     *   We also assign point values to each question. The question in the first index position has a value of 500 and
     *   the question in the last index position has a value of 100.
     */
    public void assignFiveRandomQuestions() {
    	_currentQuestionIndexes = RandomIndexes.getRandomIndexesFromList(_questions.size());

    	int pointValue = 100;
    	for(int index:_currentQuestionIndexes) {
    		Question question = _questions.get(index);
    		question.setCurrentQuestion(true);
    		question.setPointValue(pointValue);
    		pointValue += 100;
    	}
    }
    
    /*
     * This method returns how many questions have been asked from this category
     */
    public int numQuestionsAsked() {
    	int questionsAsked = 0;
    	for(int i = 0; i < _questions.size(); i++) {
    		Question question = _questions.get(i);
    		if(question.getAnswered()) {
    			questionsAsked++;
    		}
    	}
    	return questionsAsked;
    }
    
    /*
     * This method resets all the values in the question objects that were changed during the game.
     * value is reset to 0
     * answered is set to false
     * currentQuestion is set to false
     */
    public void resetQuestions() {
    	for(Question question:_questions) {
    		question.setAnswered(false);
    		question.setCurrentQuestion(false);
    		question.setPointValue(0);
    		question.resetWrongRight();
    	}
    }
    
    
    /* 
     * This method is called form the games module on the category class whenever the user wants to reload the 
     * previous game state.
     * 
     * It iterate through the questions in the category and assigns the questions their respective places in the current questions array
     * It returns a boolean indicating whether this category has current questions or not 
     */
    @SuppressWarnings("unused")
	public Boolean reload() {
    	int i = 0;
    	int pointValue = 100;
    	Boolean currentCategory = false;
    	for(Question question:_questions) {
    		for(Question _question:_questions) {
        		if(_question.getCurrentQuestion() && _question.getQuestionValue() == pointValue) {
        			// if this condition is met then we have found the lowest value question which was previously assigned to be asked
        			// so we add it back to the index array and break the inner loop 
        			
        			// set the return value to true
        			currentCategory = true;
        			
        			// add the question index to the indexes array and increment the counter
        			_currentQuestionIndexes[i] = _questions.indexOf(_question);
        			i++;
        			
        			// increment the points value
        			pointValue += 100;
        			
        			// we break the inner loop and now look for the next question which has the incremented point value
        			break;
        		}
    		}

    	}
    	
    	return currentCategory;
    }

    /*
     * Getter methods
     */
    
    public String getCategoryTitle() {
    	return _title;
    }
    
    public int[] getCurrentQuestionIndexes() {
    	return _currentQuestionIndexes;
    }
}

