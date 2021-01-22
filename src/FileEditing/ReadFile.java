package FileEditing;

import main.*;
import score.Score;
import score.ScoreBoard;

import java.io.*;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

/**
 * This class reads text files in categories folder and make instances of
 * Question and Category classes that are used in Quinzical app.
 *
 * Author: Fuki Babasaki
 */
public class ReadFile {

    /**
     * This method reads category files from src/categories
     * and returns a list of Category objects.
     * @return a list of Category objects.
     */
    public static List<Category> ReadCategoryFile(String region){
        File folder = getPath();
        // This is the path to categories folder.
        String catPath = folder.getParentFile().getAbsolutePath() + File.separator + "categories/" + region;

        return ReadFiles(catPath);
    }

    /**
     * This method read category files from src/saved_data
     * and returns a list of Category objects. Question objects of each Category object
     * may be answered depending on previous state of game.
     * @return a list of Category objects.
     */
    public static List<Category> ReadSaveFile(String region){
        File folder = getPath();

        //This is path to saved_data folder
        String savePath = folder.getParentFile().getAbsolutePath() + File.separator + "saved_data" + File.separator + region;

        return ReadFiles(savePath);
    }

    /**
     * This method reads one winning file from src/saved_data
     * and returns integer of winnings from previous game.
     * @return winnings.
     */
    public static int ReadWinnings(String region){
        File folder = getPath();

        //This is path to winning file
        String winningPath = folder.getParentFile().getAbsolutePath() + File.separator + "saved_data" + File.separator + region + File.separator + ".winnings";

        BufferedReader br = null;
        try {
            br = new BufferedReader(new InputStreamReader(new FileInputStream(winningPath)));
        } catch (FileNotFoundException e) {
        	// file does not exist so we return -1
        	return -1;
            //do nothing
        }

        String winning = null;
        try {
            winning = br.readLine();
            br.close();
        } catch (IOException e) {
            //do nothing
        }
        
        if(winning == null) {
        	return -1;
        }
        
        return Integer.parseInt(winning);
    }
    
    /*
     * 
     */
    public static ScoreBoard readScores() {
        	// path to score file
        	String savePath = ReadFile.getPath().getParentFile().getAbsolutePath() + File.separator + "saved_data" + File.separator + "scores";
        	
        	// create a ScoreBoard object
        	ScoreBoard scoreboard = new ScoreBoard();
        	
            File file = new File(savePath);
			if(file.exists()){
				// we can now read the data
				BufferedReader br = null;
				try {
			        //This reader reads each line in the category file
			        br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
			    } catch (FileNotFoundException e) {
			        //do nothing
			    }
				String line = null;
			    while(true){
			        try {
			            line = br.readLine();
			        } catch (IOException e) {
			            //Do nothing
			        }

			        if(line == null){
			            break;
			        }
			        
			        // now we process the line
			        String[] strings = splitString(line, false);
			        
			        Boolean region = false;
			        if(strings[2].equals("true")) {
			        	region = true;
			        }
			        
			        Score score = new Score(strings[0], Integer.parseInt(strings[1]), region);
			        scoreboard.addScore(score);
			    }
			}
			return scoreboard;
    }

    /**
     * This is a static template method that reads files in a specified folder
     * and makes a list of category objects.
     * @return list of category objects that contain questions
     */
    private static List<Category> ReadFiles(String path) {

        List<Category> categories = new ArrayList<>();

        File folder = new File(path);
        File[] listOfCategories = folder.listFiles();

        for(File file : listOfCategories){
            if (file.isFile()){
            	
                //This is path to each category.
                String catPath = path + File.separator + file.getName() + "/";

                //In save_data, there is .winnings file that is not category
                if(file.getName().equals(".winnings")){
                    continue;
                }

                //Create an instance of Category class
                Category category = new Category(file.getName());
                categories.add(category);

                BufferedReader br = null;
                try {
                    //This reader reads each line in the category file
                    br = new BufferedReader(new InputStreamReader(new FileInputStream(catPath)));
                } catch (FileNotFoundException e) {
                    //do nothing
                }

                String line = null;
                
                while(true){
                    try {
                        line = br.readLine();
                    } catch (IOException e) {
                        //Do nothing
                    }

                    if(line == null){
                        break;
                    }
                    
                    
                    Question question;
                    // if the line contains !, @ or #, then we know that it is a current question
                    if(line.contains("!") | line.contains("@") | line.contains("#")) {
                    
                    	// we split the string into its question, answer and point value parts
                        String[] strings = splitString(line, true);
                        question = new Question(processQuestion(strings[0]), strings[1]);
                        // set the point value associated with the question
                        question.setPointValue(Integer.parseInt(strings[2]));

                        // the question is currently being asked
                    	question.setCurrentQuestion(true);

                    	
                    	if(line.contains("@")){
                    		// if the line contains the @ symbol then we know that the question has already been asked and got it wrong
                    		question.setAnswered(true);
                            question.setWrong();
                        }else if(line.contains("#")){
                        	// if the string contains the # symbol then the user got the question right
                            question.setAnswered(true);
                            question.setRight();
                        }
                    }else {
                    	// otherwise the question has no special attributes so can just be read in two parts
                    	int index = line.indexOf('|');
                    	String questionString = line.substring(0, index);
                    	String answerString = line.substring(index+1, line.length());
                    	
                    	question = new Question(processQuestion(questionString), answerString);
                    }

                    //Add the question object to its category object
                    category.addQuestion(question);
                }
            }
        }
        return categories;
    }


    /**
     * This method returns the path to jar file
     * @return
     */
    public static File getPath(){
        File folder = null;
        try {
            //get path to this class file
            folder = new File(ReadFile.class.getProtectionDomain().getCodeSource()
                    .getLocation().toURI().getPath());
        } catch (URISyntaxException e) {
            //do nothing
        }

        // This is the path to categories folder.
        return folder;
    }

    
    /*
     * This method removes the excess commas of the question
     */
    private static String processQuestion(String question){
        question = question.trim();
        for(int i = question.length() - 1; i >= 0; i--){
            if(question.charAt(i) == '|'){
                question = question.substring(0, i);
            }else {
                break;
            }
        }

        return question;
    }
    
    /*
     * this method is called when the question has a special symbol at the front to indicate that
     * it has a question, answer and point value part.
     * 
     * It returns an array of strings containing the parts
     */
    public static String[] splitString(String line, Boolean isCharacterAtFront) {
    	String[] array = new String[3];
    	
    	int index = line.indexOf('|');
    	String questionString = line.substring(0,index);
    	
    	if(isCharacterAtFront) {
    		questionString = questionString.substring(1, questionString.length()).trim();
    	}
    	
    	
    	String middleToEnd = line.substring(index+1, line.length());
    	int index1 = middleToEnd.indexOf('|');
    	String answerString = middleToEnd.substring(0,index1);
    	String value = middleToEnd.substring(index1 + 1, middleToEnd.length());
    	
    	
    	array[0] = questionString;
    	array[1] = answerString;
    	array[2] = value;
    	
    	return array;
    }
}
