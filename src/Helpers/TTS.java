package Helpers;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

/**
 * This class controls use of espeak in command line.
 */
public class TTS {
    private int _speed;
    private int _scale;
    private Process _process;
    private Boolean processDone = false;
    private Boolean processCancelled = false;

    public TTS(){
        //175 wpm is the default speed.
        _speed = 175;
        _scale = 5;
    }

    /**
     * This runs espeak with the speech speed specified. It destroys the previous task (if any)
     * So the preceding sound does not overlap.
     * @param sentence
     */
    public void threadSafeSpeaking(String sentence) {
    	new Thread() {
    		public void run() {
    			setProcessCancelled(false);
    			if(_process != null){
    			    _process.destroy();
    			    setProcessCancelled(true);
                }

    	    	// speak the question to the user
    	    	ProcessBuilder  builder = new ProcessBuilder("espeak", "-s", Integer.toString(_speed));
    	    	try {
    				_process = builder.start();
    				processDone = false;
    			    //ProcessListener listner = new ProcessListener(_process);
    				OutputStream in = _process.getOutputStream();
    				PrintWriter stdin = new PrintWriter(in);
    				stdin.println(sentence);
    				stdin.close();
    				_process.waitFor();
    				processDone = true;
    	    	} catch (IOException e) {
    			} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
    		}
    	}.start();
    }
    /**
     * This method sets the speed of speech in a scale of 1 to 10.
     * The default speed is 5 which is 175 wpm.
     * The input is integer that is a scale of speed.
     */
    public void setSpeed(int scale) {
        _scale = scale;

        if(scale < 1 || scale > 10){
            throw new IllegalArgumentException("Error: scale must be between 1 and 10");
        }

        int speed = (scale - 5)*25;

        _speed = 175 + speed;
    }

    /**
     * This is a getter method to return the value of scale
     */
    public int getScale(){
        return _scale;
    }
    public Boolean getProcessCancelled() {
    	return processCancelled;
    }
    
    public Boolean getProcessState() {
    	return processDone;
    }
    
    public void setStateFalse() {
    	processDone = false;
    }
    
    public void setProcessCancelled(Boolean state) {
    	processCancelled = state;
    }
    
    public void destroyPreviousProcess() {
    	if(_process != null) {
    		_process.destroy();
    	}
    }
    
    
}
