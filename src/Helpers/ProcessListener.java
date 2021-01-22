package Helpers;

import java.util.EventListener;

public interface ProcessListener extends EventListener{
	public void processFinished(Process process);
}
