package score;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ScoreBoard {

	private static List<Score> _scores;
	
	public ScoreBoard(){
		_scores = new ArrayList<Score>();
	}
	
	public void addScore(Score newScore) {
		_scores.add(newScore);
		
		sortScores();
	}
	
	private static void sortScores() {
		// create temporary lists to store separate nz scores and international scores
		ArrayList<Score> nzScore = new ArrayList<Score>();
		ArrayList<Score> intScore = new ArrayList<Score>();
		
		// move all scores from main list into each list
		for(Score score:_scores) {
			if(score.getWhichRegion()) {
				// add to nz list
				nzScore.add(score);
			}else {
				// add to international region
				intScore.add(score);
			}
		}
		
		// now we can sort each individual list via bubble sort
		for(Score currentScore:nzScore) {
			for(int i = 0; i < nzScore.size() - 1; i++) {
				// get score at position i
				Score score = nzScore.get(i);
				
				// get score in position i+1
				Score score1 = nzScore.get(i+1);
				
				if(score.getScore() < score1.getScore()) {
					// we now swap elements
					Collections.swap(nzScore, i, i+1);
				}
			}
		}
		
		// now we can sort each individual list via bubble sort
		for(Score currentScore:intScore) {
			for(int i = 0; i < intScore.size() - 1; i++) {
				// get score at position i
				Score score = intScore.get(i);
				
				// get score in position i+1
				Score score1 = intScore.get(i+1);
				
				if(score.getScore() < score1.getScore()) {
					// we now swap elements
					Collections.swap(intScore, i, i+1);
				}
			}
		}
		
		// now we can merge the two lists back together
		nzScore.addAll(intScore);
		_scores = nzScore;
	}
	
	public int getScoreListSize() {
		return _scores.size();
	}
	
	public Score getScoreAtIndex(int index) {
		return _scores.get(index);
	}
}
