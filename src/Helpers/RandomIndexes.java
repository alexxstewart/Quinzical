package Helpers;

import java.util.Random;
import java.util.stream.IntStream;

public class RandomIndexes {
	
	
	/**
	 * This method is a static method accessed from both the gamesModule (when determining the 5 category to ask) and 
	 * the Category class (when determining which 5 questions to ask)
	 */
	public static int[] getRandomIndexesFromList(int listSize) {

		if(listSize < 5){
			throw new IllegalArgumentException("Error: number of questions must be more than or equal to 5");
		}

		Random random = new Random();
		int[] indexes = new int[]{-1, -1, -1, -1, -1};
		for(int i = 0; i < 5; i++) {
			Boolean stillLoop = true;
			while(stillLoop) {
				// generate random number between range 
				int randomNum = random.nextInt(listSize-0);
				
				// check if the number generated is already an index in the array
				Boolean match = IntStream.of(indexes).anyMatch(x -> x == randomNum);
				if(match) {
					// if the number isn't unique then we keep looping
					stillLoop = true;
				}else {
					// if the number is unique then we add it to the array and we break the loop
					indexes[i] = randomNum;
					stillLoop = false;
				}
			}
		}
		return indexes; 
	}
}
