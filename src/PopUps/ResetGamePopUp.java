package PopUps;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import main.GamesModule;
import main.MainPage;

public class ResetGamePopUp extends PopUp{

	private GamesModule _module;
	private Stage _stage;
	private MainPage _mainPage;
	
	public ResetGamePopUp(GamesModule module, Stage stage, MainPage mainPage) {
		_module = module;
		_stage = stage;
		_mainPage = mainPage;
	}
	
	@Override
	protected List<Button> createButtons(Stage stage) {
		List<Button> list = new ArrayList<Button>();
		
		Button resetButton = new Button("Reset Game");
		Button backButton = new Button("Go Back");
		resetButton.setId("button");
		backButton.setId("button");
		resetButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_module.resetGame();
				_mainPage.lock();
				_stage.close();
			}
		});
		
		backButton.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				_stage.close();
			}
		});
		
		list.add(resetButton);
		list.add(backButton);
		return list;
	}

	@Override
	protected Text createMessage() {
		return new Text("Are You sure you want to reset the game? \nThe international module will be locked again");
	}

}
