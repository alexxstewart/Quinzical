package score;

public class Score {
	private String _name;
	private int _score;
	private Boolean _isNzScore;
	
	public Score(String name, int score, Boolean isNzScore){
		_name = name;
		_score = score;
		_isNzScore = isNzScore;
	}
	
	public String getName() {
		return _name;
	}
	
	public int getScore() {
		return _score;
	}
	
	public Boolean getWhichRegion() {
		return _isNzScore;
	}
}
