package main;

import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.TilePane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;
import style.AppStyle;

import java.io.File;
import java.io.IOException;

/**
 * This class creates a category page to add or remove a category.
 */
public class AddCategoryPage {
    Stage _stage;
    BorderPane _pane;
    Scene _scene;
    TilePane _tilePane;

    public AddCategoryPage(Stage stage){
        _stage = stage;

        _stage.setTitle("Quinzical");

        _pane = new BorderPane();
        AppStyle.setPaneStyle(_pane);

        _scene = new Scene(_pane,900,700, Color.WHITE);
        _scene.getStylesheets().add("style/style.css");

        // create title and set style
        Text titleText = new Text("Quinzical");
        AppStyle.styleQBTitle(titleText, true);

        VBox vbox = new VBox();
        vbox.setAlignment(Pos.CENTER);

        Text categories = new Text("categories");
        AppStyle.styleQBTitle(categories, false);

        ScrollPane scrollPane = new ScrollPane();
        scrollPane.setPrefSize(600, 400);
        AppStyle.setScrollPaneStyle(scrollPane);

        _tilePane = new TilePane();
        _tilePane.setPrefSize(500, 400);
        _tilePane.setMaxWidth(400);
        AppStyle.setPaneStyle(_tilePane);
        scrollPane.setContent(_tilePane);
        addCategories();

        vbox.getChildren().addAll(categories, scrollPane);

        _pane.setCenter(vbox);

        _stage.setScene(_scene);
        _stage.show();
    }

    private void addCategories(){
        _tilePane.getChildren().addAll(new Button("category 1"), new Button("category 2"), new Button("category 3"));
    }
}
