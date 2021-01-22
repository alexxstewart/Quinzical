package FileEditing;

import main.*;
import score.Score;
import score.ScoreBoard;

import java.io.*;
import java.util.List;


/*
 * This class handles the functionality of saving the game state to the folder saved_data
 * 
 * It also has a method which deletes all the saved data
 */
public class WriteFile {
	
	
    /**
     * This method writes save data files to src/save_data
     */
    public static void writeFile(List<Category> categories, int winnings, String region){

        //this is path to save data files
        String savePath = ReadFile.getPath().getParentFile().getAbsolutePath() + File.separator + "saved_data/" + region;

        try {
            PrintWriter pr = new PrintWriter(new FileWriter(savePath + File.separator + ".winnings"));
            // write winnings to .winnings file
            pr.println(winnings + "");

            pr.close();
        } catch (IOException e) {
        	System.out.println(e);
            //do nothing
        }

        for(Category category : categories){
            PrintWriter pr = null;
            try {
                File file = new File(savePath + File.separator, category.getCategoryTitle());
                if(!file.exists()){
                    file.createNewFile();
                }

                pr = new PrintWriter(new FileWriter(file));
            } catch (IOException e) {
                e.printStackTrace();
            }

            for(Question question : category.getQuestions()){
                String line = "";
                
                // if the question has been assigned to be asked but has not been asked/answered yet then we append ! at the start
                if(question.getCurrentQuestion() && !question.getAnswered()){
                	line += "!";
                }
                // if the question has been asked and the user got the question wrong then we append @
                else if(question.getAnswered() && question.gotWrong()) {
                	line += "@";
                }
                // if the question has been asked and the user got it right then we append #
                else if(question.gotRight()) {
                	line += "#";
                }

                // we now add the question and the answer to the line with a comma inbetween
                line += question.getQuestion() + "| " + question.getPreProccessedAnswer();

                // if the question has been assigned to be asked we always add the point value at the end.
                if(question.getCurrentQuestion()) {
                	line += "|" + question.getQuestionValue();
                }
                pr.println(line);
            }

            // close the reader
            pr.close();
        }
    }

    /**
     * This method removes save files in saved_data
     */
    public static void removeSaves(String region){
        //this is path to save data files
        String savePath = ReadFile.getPath().getParentFile().getAbsolutePath() + File.separator + "saved_data/" + region;

        File dir = new File(savePath);
        for(File file : dir.listFiles()){
            //if it is .winnings txt file, then empty the file.
            if(file.getName().equals(".winnings")){
                PrintWriter pr = null;
                try {
                    pr = new PrintWriter(file);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                pr.print("");
                pr.close();
            }else {
                //Remove other files.
                file.delete();
            }
        }
    }
    
    public static void writeHighScores(ScoreBoard scores) {
    	
    	// path to score file
    	String savePath = ReadFile.getPath().getParentFile().getAbsolutePath() + File.separator + "saved_data/scores";
    	
    	PrintWriter pr = null;
        try {
            File file = new File(savePath);
            if(file.exists()){
                file.delete();
            }
            
            file.createNewFile();

            pr = new PrintWriter(new FileWriter(file));
        } catch (IOException e) {
            e.printStackTrace();
        }
        
  
        for(int i = 0; i < scores.getScoreListSize(); i++){
        	Score score = scores.getScoreAtIndex(i);
        	String line = score.getName() + "|" + score.getScore() + "|" + score.getWhichRegion();
            pr.println(line);
        }

        // close the reader
        pr.close();
    }
}
