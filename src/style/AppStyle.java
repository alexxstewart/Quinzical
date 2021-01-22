package style;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import font.FontLoader;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.text.Text;

/*
 * This class handles the styling of the application
 */
public class AppStyle {

	public static void textColor1(Text text) {
		text.setFill(Color.AQUA);
	}
	
	public static void textColorWhite(Text text) {
		text.setFill(Color.WHITE);
	}
	
	public static void textColorRed(Text text) {
		text.setFill(Color.web("#FF563C"));
	}
	
	public static void textColorGrey(Text text) {
		text.setFill(Color.web("#A0A0A0"));
	}
	
	public static void textColorGreen(Text text) {
		text.setFill(Color.web("#509562"));
	}
	
	public static void styleSmallText(Text text) {
		text.setFill(Color.web("#A0A0A0"));
		text.setStyle("-fx-font: 12 arial;");
	}
	
	public static void styleReminderText(Text text) {
		text.setFill(Color.web("#FF0000"));
		text.setStyle("-fx-font: 20 arial;");
	}
	
	public static void styleQBTitle(Text text, boolean isMainTitle) {
		double size = 50.0;
		if(isMainTitle){
			size = 100;
		}
		FontLoader fl =  new FontLoader("fonts" + File.separator +"ITC Korinna Regular.ttf", size);
		text.setFont(fl.getFont());
		text.setFill(Color.WHITE);
	}
	
	public static void styleQuestionButton(Button button) {
		button.setStyle("-fx-background-color: #9c9c9c;");
		
	}
	
	public static void styleNormalText(Text text) {
		text.setFill(Color.web("#FFFFFF"));
		text.setStyle("-fx-font: 24 arial;");
	}
	
	public static void styleCategoryTitle(Text text) {
		text.setStyle("-fx-font: 20 arial;");
		text.setFill(Color.WHITE);
	}
	
	public static void styleFinalTitle(Text text) {
		text.setFill(Color.web("#FFFFFF"));
		text.setStyle("-fx-font: 60 arial;");
	}
	
	public static void styleScoreText(Text text) {
		text.setFill(Color.web("#FFFFFF"));
		text.setStyle("-fx-font: 34 arial;");
	}
	
	public static void styleSaveScoreText(Text text) {
		text.setFill(Color.web("#FFFFFF"));
		text.setStyle("-fx-font: 18 arial;");
	}
	
	public static void setPaneStyle(Pane pane) {
		//pane.setStyle("-fx-background-color: #495159");
		pane.setStyle("-fx-background-color: #2C2F33");
	}
	
	public static void setScrollPaneStyle(ScrollPane pane) {
		pane.setStyle("-fx-background: #495159");
	}
	
	public static void styleButton(Button button) {
		button.setStyle("-fx-cursor: hand;");
	}
	
	public static void styleLockedButton(Button button) {
		button.setStyle("-fx-background-color: red;");
	}
	
	public static void styleNormalButton(Button button) {
		button.setStyle("");
	}
	public static void styleButtonGreen(Button button) {
		button.setStyle("-fx-background-color:#509562;"
				+ "-fx-border-color: black;"
				+ "-fx-cursor: hand;"
				+ "-fx-background-image: url('style/tick.png')");
	}
	
	public static void styleButtonGrey(Button button) {
		button.setStyle("-fx-background-color:#808080");
		button.setStyle("-fx-border-color: black");
	}
	
	public static void styleSlider(Slider slider) {
		slider.setStyle("-fx-cursor: hand;");
	}
	
	public static void styleBigButton(Button button, String color) {
		String style = "-fx-background-color: " + color + ";" 
				+ "-fx-text-fill: black;"
				+ "-fx-font-size: 16;"
				+ "-fx-border-radius: 0;";
		
		button.setStyle(style);
	}
	
	public static List<String> createColorList(){
		List<String> colors = new ArrayList<String>();
	
		colors.add("#ff9900"); // orange
		colors.add("#00c912"); // green
		colors.add("#c4006c"); // pink/ magenta
		colors.add("#3400c4"); // dark blue
		colors.add("#00c9cc"); // light blue
		colors.add("#db1600");
		colors.add("#ebe700");
		
		return colors;
	}
	
	public static List<String> lighterColors(){
		List<String> colors = new ArrayList<String>();
		
		colors.add("#ffda82"); // light orange
		colors.add("#adffb0"); // light green / mint
		colors.add("#ffadbb"); // light pink
		colors.add("#aeadff"); // light purple
		colors.add("#adfcff"); // light blue
		
		return colors;
	}
}
