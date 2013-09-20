import java.io.IOException;


public class LogData {
    
    public boolean loadLogs(String path) throws IOException{
        
        //FIXME--Check if file or folder | Make use of file object to check better 
        if(path.substring(path.length()-3) == "csv"){
            loadLogFile(path);
        }else{
            loadLogFolder(path);
        }
        
        return true;
    }
    
    private boolean loadLogFile(String filePath) throws IOException{
        
        Log log = new Log(filePath);
        
        return true;
    }
    
    private boolean loadLogFolder(String folderPath) throws IOException{
        
        String[] fileList = new String[5];
        
        for(int c=0; c < fileList.length;c++){
            loadLogFile(fileList[c]);
        }
        
        return true;
    }
    
}
