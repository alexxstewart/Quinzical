package font;

import javafx.scene.text.Font;
import javafx.scene.text.Text;

import java.io.File;
import java.io.InputStream;

/**
 * This class loads font to reduce duplicate code.
 */
public class FontLoader {
    private Font _font;
    private String _fileLocation;
    private Double _fontSize;

    public FontLoader(String fileLocation, Double fontSize){
        _fileLocation = fileLocation;
        _fontSize = fontSize;
        loadFile();
    }

    private void loadFile(){
        InputStream fontStream = FontLoader.class.getResourceAsStream(_fileLocation);
        _font = null;
        if(fontStream != null){
            _font = Font.loadFont(fontStream, _fontSize);
        }
    }

    public void setFontSize(Double fontSize){
        _fontSize = fontSize;
        loadFile();
    }

    public Font getFont(){
        return _font;
    }
}
