import java.io.IOException;

import java.io.File;


public class LogGroup {

    //--Fields
    private File logPath;
    private Log[] logs;
    
    public LogGroup(String path) throws IOException{
        
        this.logPath = new File(path);
        
        //--Check if file or folder 
        if(this.logPath.isFile()){
            loadLogFile(path);
        }else{
            loadLogFolder(path);
        }
        logs[0].printTemps();
        
    }
    
    private boolean loadLogFile(String filePath) throws IOException{
        logs = new Log[1];
        logs[0] = new Log(filePath);
        
        return true;
    }
    
    private boolean loadLogFolder(String folderPath) throws IOException{
        
        String[] fileList = this.logPath.list();
        
        int numLogs = 0;
        
        for(int c=0; c < fileList.length;c++){
            if( fileList[c].substring(fileList[c].length()-3).equals("csv") ){
                numLogs++;
                
            }
        }

        logs = new Log[numLogs];
        for(int c=0; c < fileList.length;c++){
            if( fileList[c].substring(fileList[c].length()-3).equals("csv") ){
                logs[0] = new Log(folderPath  + fileList[c]);
                
            }
        }
        
        return true;
    }
    
}
