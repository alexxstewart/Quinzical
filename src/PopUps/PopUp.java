package PopUps;
import style.*;

import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.List;


/*
 * This class is the abstract class which handles all of the core functionality for each of the
 * three pop ups 
 */
public abstract class PopUp extends Application {

    @Override
    public void start(Stage stage){
        // create buttons
        List<Button> buttons = createButtons(stage);

        //add the buttons to a hbox
        HBox hbox = new HBox(10);
        hbox.setAlignment(Pos.CENTER);
        hbox.getChildren().addAll(buttons);

        // create a text element
        Text text = createMessage();
        AppStyle.styleNormalText(text);

        // create a tile pane
        BorderPane pane = new BorderPane();
        AppStyle.setPaneStyle(pane);

        // create vbox to store the text and buttons
        VBox vbox = new VBox(20);
        vbox.setAlignment(Pos.CENTER);
        vbox.getChildren().addAll(text, hbox);

        pane.setCenter(vbox);

        // create a scene
        Scene scene = new Scene(pane, 700, 200);

        // set the scene
        stage.setScene(scene);

        stage.show();
    }

    // methods to be implemented in class
    protected abstract List<Button> createButtons(Stage stage);

    protected abstract Text createMessage();
}
