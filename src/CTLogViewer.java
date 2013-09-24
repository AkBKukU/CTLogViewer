import java.io.IOException;


public class CTLogViewer {

    //--Field Declarations
    public static LogViewGui window;
    public static LogGroup logs;
    
    public static void main(String[] args) throws IOException{
        
        //--Create window
        //window = new LogViewGui();
        //--With Session end "/home/akbkuku/CT-Log 2013-09-19 19-58-47.csv"
        //--Without "/media/Windows/Program Files/Core Temp/CT-Log 2013-09-18 09-10-37.csv"
    	// Windows C:\Program Files\Core Temp
        LogGroup test = new LogGroup("/home/akbkuku/Dropbox/Core Temp/");
        // System.out.println("Logs Found:" + test.getNumLogs());
        test.printTemps();
    }
}
