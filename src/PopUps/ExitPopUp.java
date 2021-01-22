package PopUps;

import style.*;
import FileEditing.*;
import main.*;

import java.util.ArrayList;
import java.util.List;

import javafx.scene.control.Button; 
import javafx.scene.text.Text;
import javafx.event.ActionEvent; 
import javafx.event.EventHandler;
import javafx.stage.Stage; 
   

/*
 * This popup extends the abstract class PopUp which provides the core functionality
 * 
 * This popup is displayed when the user has started the gamesModule but then clicks the top right exit button
 * 
 * The popup gives them the options:
 * - return to the previous screen
 * - quit the game without saving
 * - save and quit the game
 */
public class ExitPopUp extends PopUp {
	private Stage _oldStage;
	private List<Category> _categories;
	private int _winnings;
	private String _region;
	
	public ExitPopUp(Stage oldStage, List<Category> list, int winnings, String region) {
		_oldStage = oldStage;
		_categories = list;
		_winnings = winnings;
		_region = region;
	}

    protected List<Button> createButtons(Stage stage){
	    List<Button> buttons = new ArrayList<>();
	    buttons.add(createQuitButton(stage, _oldStage));
	    buttons.add(createReturnButton(stage));
	    buttons.add(createSaveGameButton(stage, _oldStage));

	    return buttons;
    }

    protected Text createMessage(){
	    return new Text("Are you sure you want to exit without saving");
    }

    private Button createQuitButton(Stage stage, Stage oldStage) {
        Button button = new Button("Quit");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	// remove all previous saves
            	WriteFile.removeSaves(_region);
            	
            	// quit both scenes
            	stage.close();
            	oldStage.close();
            }
        });
        return button;
    }
    
    private Button createReturnButton(Stage stage) {
        Button button = new Button("Return");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	stage.close();
            }
        });
        return button;
    }

    private Button createSaveGameButton(Stage stage, Stage oldStage) {
        Button button = new Button("Save and Quit");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
            	// save game
            	WriteFile.writeFile(_categories, _winnings, _region);
            	
            	// quit application
            	stage.close();
            	oldStage.close();
            }
        });
        return button;
    }

}