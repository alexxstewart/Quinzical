package PopUps;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class UnlockNotificationPopUp extends PopUp{

	private Stage _stage;
	
	public UnlockNotificationPopUp(Stage stage){
		_stage = stage;
	}
	
	@Override
	protected List<Button> createButtons(Stage stage) {
		List<Button> list = new ArrayList<Button>();
		Button button = new Button("OK"); 
		button.setId("button");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_stage.close();
			}
		});
		list.add(button);
		return list;
	}

	@Override
	protected Text createMessage() {
		return new Text("You have unlocked the International Section!");
	}

}
