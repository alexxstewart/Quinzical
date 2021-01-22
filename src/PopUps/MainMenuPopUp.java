package PopUps;

import main.*;
import FileEditing.*;
import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button; 
import javafx.scene.text.Text;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler;
import javafx.stage.Stage; 
  

/*
 * This pop up extends the abstract class Popup
 * 
 * It is displayed when the user has already answered a question in the GamesModule and wants to return to the main menu
 * 
 * So we must ask them if they want to save the game or not before returning to the main menu
 */

public class MainMenuPopUp extends PopUp {
	private List<Category> _categories;
	private int _winnings;
	private String _region;
	
	public MainMenuPopUp(List<Category> list, int winnings, String region) {
		_categories = list;
		_winnings = winnings;
		_region = region;
	}


	@Override
    protected List<Button> createButtons(Stage stage){
        stage.setTitle("Confirmation");

        List<Button> buttons = new ArrayList<>();

        // the yes button saves the game state on click and returns to main menu
        buttons.add(createYesButton(stage, _categories, _winnings, _region));
        
        // the no button resets the game state on click and returns to main menu
        buttons.add(createNoButton(stage));

        return buttons;
    }

    @Override
    protected Text createMessage(){
        return new Text("Do you want to save your game?");
    }

    private Button createYesButton(Stage stage, List<Category> list, int winnings, String region) {
        Button button = new Button("Yes");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	WriteFile.writeFile(list, winnings, region);
            	stage.close();
            }
        });
        return button;
    }
    
    private Button createNoButton(Stage stage) {
        Button button = new Button("No");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
        		
            	for(Category category:_categories) {
        			category.resetQuestions();
        		}
        		
            	WriteFile.removeSaves(_region);
            	stage.close();
            }
        });
        return button;
    }
   
}