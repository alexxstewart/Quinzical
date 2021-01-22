package Helpers;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.control.Slider;

/*
 * This class has two methods which help to control the media functionality of the game
 */
public class Media {
	
	/*
	 * This static method takes a TTS object as input and creates a slider which updates the tts speed everytime the slider moves 
	 */
	public static Slider createSlider(TTS tts) {
		Slider _slider = new Slider(1,10,5);
		_slider.setBlockIncrement(1.0);

		//If the speed is changed, set its value ot slider
		_slider.setValue(tts.getScale());
		
		_slider.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                    Number old_val, Number position) {
            			int number = (int) Math.round(position.doubleValue());
                        tts.setSpeed(number);
                }
        });
	
		return _slider;
	}
	
	/*
	 * This method makes the button required to playback the audio.
	 */
	public static Button createPlayBackButton(TTS tts, String sentence) {
		Button button = new Button("Playback Audio");
		button.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				tts.threadSafeSpeaking(sentence);
			}
		});
		return button;
	}
}
