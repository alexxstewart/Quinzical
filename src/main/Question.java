package main;

import java.text.Normalizer;
import java.util.ArrayList;

public class Question {
	private String _question;
	private Boolean _answered;
	
	// this field represents whether the question is currently being asked or not
	private Boolean _currentQuestion;
	
	// point value assigned to the question
	private int _value;
	
	// fields for whether the user got the question right or wrong
	private Boolean _right;
	private Boolean _wrong;
	
	// we have two answer types: one which is the original unchanged answer from the file and one that is processed which we use for the game
	private String _answer;
	private String _processedAnswer;
	
	public Question(String question, String answer){

		_question = question;
		_answer = answer;
		
		//Simplify answer
		_processedAnswer = simplify(answer);
		_answered = false;
		_currentQuestion = false;
		_right = false;
		_wrong = false;
	}

	/**
	 * This method takes user's answer and validate if
	 * he/she answered it correctly.
	 * @param userAnswer
	 * @return
	 */
	public boolean validateAnswer(String userAnswer){
		if(userAnswer.length() == 0) {
			return false;
		}
		
		// we now need to go through all the possible conditions with answers containing / and ,
		
		// if answer contains a / then we need to split the answer for comparison
		if(_processedAnswer.indexOf('/') != -1) {
			// we can create a store the indexes of the backslashes in an array
			ArrayList<Integer> list = new ArrayList<Integer>();
			
			// the answer may contain multiple / so we need to iterate through the string
			for(int i = 0; i < _processedAnswer.length(); i++) {
				if(_processedAnswer.charAt(i) == '/') {
					list.add(i);
				}
			}
			
			// we can now process the substrings with the indexes in the arraylist
			int lastIndex = 0;
			for(int nums:list) {
				String substring = _processedAnswer.substring(lastIndex, nums);
				if(substring.equalsIgnoreCase(userAnswer)) {
					return true;
				}
				lastIndex = nums+1;
			}
			
			// lastly check the last substring between the last index and the end of the string
			String substring = _processedAnswer.substring(lastIndex, _processedAnswer.length());
			if(substring.equalsIgnoreCase(userAnswer)) {
				return true;
			}
		}
		// now we need top check the scenario where there a commas as the answer requires multiple parts
		else if(_processedAnswer.indexOf(',') != -1) {
			
			ArrayList<String> answerList = getSubStrings(_processedAnswer, ',');
			ArrayList<String> userAnswerList = getSubStrings(userAnswer, ',');
			
			// we now have all the substrings in the array for both the answer and the users answer
			
			// if the lists are not the same size then the user answer is wrong
			if(answerList.size() != userAnswerList.size()) {
				return false;
			}
			else {
				// we now need to compare each of the substrings
				for(String answer:answerList) {
					if(!userAnswerList.contains(answer)) {
						return false;
					}
				}
			}
			return true;
		}
		else if(_processedAnswer.equalsIgnoreCase(simplify(userAnswer))) {
			return true;
		}
		
		// if none of the above conditions are true then the answer is wrong and we return false.
		return false;
	}

	/**
	 * This method sets the current question to be true as it has been selected as one of five random questions to be asked
	 */
	public void setCurrentQuestion(Boolean value) {
		_currentQuestion = value;
	}

	/**
	 * This method sets the current question answered or unanswered based on the input
	 */
	public void setAnswered(boolean answered){
		_answered = answered;
	}
	
	/*
	 * This method sets the value for the question (either 100, 200, 300, 400, 500)
	 */
	public void setPointValue(int value) {
		_value = value;
	}
	
	// this method is called when the user gets the answer wrong
	public void setWrong() {
		_wrong = true;
	}
	
	// this method is called when the user gets the answer right
	public void setRight() {
		_right = true;
	}

	// resets the right and wrong values to their default false state.
	public void resetWrongRight() {
		_wrong = false;
		_right = false;
	}
	
    /*
     * Getter methods
     */
	
	public String getQuestion() {

		return _question;
	}
	
	public String getAnswer() {
		return _processedAnswer;
	}
	
	public String getPreProccessedAnswer() {
		return _answer;
	}
	
	public Boolean getAnswered() {
		return _answered;
	}
	
	public Boolean getCurrentQuestion() {
		return _currentQuestion;
	}
	
	public int getQuestionValue() {
		return _value;
	}

	public boolean gotRight() {
		return _right;
	}
	
	public boolean gotWrong() {
		return _wrong;
	}
	
	/*
	 * this method simplifies the input answer string. It does:
	 * - removes leading words such as 'a' and 'the'
	 * - trims whitespace around the string
	 * - converts Maori characters to latin characters
	 */
	private String simplify(String answer){

		int closeBracketPos = answer.indexOf(')');
		
		String newAnswer = answer.substring(closeBracketPos+1);
		
		// remove words such as 'the', 'a'
		newAnswer = newAnswer.replace(" the ", "");
		newAnswer = newAnswer.replace(" a ", "");
		
		//remove any leading spaces
		newAnswer = newAnswer.trim();

		//replace any period at the end
		while(newAnswer.charAt(newAnswer.length() - 1) == '.'){
			newAnswer = newAnswer.substring(0, newAnswer.length()-1);
		}
		
		// replace non-latin characters such as ā, ī, ō, ū
		StringBuilder sb = new StringBuilder(newAnswer.length());
		newAnswer = Normalizer.normalize(newAnswer, Normalizer.Form.NFD);
		for (char c : newAnswer.toCharArray()) {
            if (c <= '\u007F') sb.append(c);
        }
		newAnswer = sb.toString();

		return newAnswer;
	}
	
	/*
	 * This method is called when an answer has multiple sections to it such as:
	 * - if the answer has a / then it answers either side of the / are correct
	 * - it it has a , then the user answer must contain all answers either side of the comma
	 * 
	 * This method returns a list containing the string sub-answers
	 */
	private ArrayList<String> getSubStrings(String string, char splitChar){
		ArrayList<String> list = new ArrayList<String>();
		int lastIndex = -1;
		for(int i = 0; i < string.length(); i++) {
			if(string.charAt(i) == splitChar) {
				String substring = string.substring(lastIndex+1, i).trim().toLowerCase();
				list.add(substring);
				lastIndex = i;
			}
		}
		String substring = string.substring(lastIndex+1, string.length()).trim().toLowerCase();
		list.add(substring);
		
		return list;
	}
}
