package PopUps;

import style.*;
import FileEditing.*;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.util.ArrayList;
import java.util.List;
/*
 * This class is used when a category file has less than 5 questions in it.
 * 
 * This would cause errors in the Games Module so this pop up is created which
 * forces the user to quit the game and fix the error
 * 
 *  
 */

public class ErrorPopUp extends PopUp {

    private Stage _oldStage;
    private String _region;

    public ErrorPopUp(Stage oldStage, Stage stage, String region) {
        _oldStage = oldStage;
        _region = region;
        
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
  	      public void handle(WindowEvent we) {
  	    	  // consume the exit event then exit all stages
  	    	  we.consume();
	          stage.close();
	          oldStage.close();
  	      }
  	  });
    }

    protected List<Button> createButtons(Stage stage){
        List<Button> buttons = new ArrayList<>();
        buttons.add(createQuitButton(stage, _oldStage));

        return buttons;
    }

    /*
     * createMethods is called from the PopUp class which sets the text to be displayed on the screen
     */
    protected Text createMessage(){
        Text text = new Text("Error \n \nThere should be 5 or more questions in each category");
        text.setWrappingWidth(400);

        return text;
    }

    private Button createQuitButton(Stage stage, Stage oldStage) {
        Button button = new Button("Quit");
        button.setId("button");
        button.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // we just want to quit the popup and quit the game
                WriteFile.removeSaves(_region);
                stage.close();
                oldStage.close();
            }
        });
        return button;
    }

}
