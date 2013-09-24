import java.io.IOException;

import java.io.File;


public class LogGroup {

    //--Fields
    private File logPath;
    private Log[] logs;
    private int[] highestTemps;
    private int[] lowestTemps;
    private int numCores = 0;
    private int numLogs = 0;
    
    
    public LogGroup(String path) throws IOException{
        
        this.logPath = new File(path);
        
        //--Check if file or folder 
        if(this.logPath.isFile()){
            loadLogFile(path);
        }else{
            loadLogFolder(path);
        }
        
    }
    
    private boolean loadLogFile(String filePath) throws IOException{
        logs = new Log[1];
        logs[0] = new Log(filePath);
        
        this.highestTemps = logs[0].getHighTemp();
        this.lowestTemps = logs[0].getLowTemp();
        
        return true;
    }
    
    private boolean loadLogFolder(String folderPath) throws IOException{
        
        String[] fileList = this.logPath.list();
        
        this.numLogs = 0;
        String validLogs = "";
        
        for(int c=0; c < fileList.length;c++){
            if( fileList[c].substring(fileList[c].length()-3).equals("csv") ){
                Log checkLog = new Log (folderPath  + fileList[c]);
                
                if(checkLog.isValid()){
                    numLogs++;
                    validLogs = validLogs + fileList[c] + ",";
                }
                
                
            }
        }
        boolean getCores = true;
        int logIndex = 0;
        
        logs = new Log[this.numLogs];
        for(int c=0; c < fileList.length;c++){
            
            if( fileList[c].substring(fileList[c].length()-3).equals("csv") && validLogs.contains(fileList[c]) ){
                logs[logIndex] = new Log(folderPath  + fileList[c]);
                if(getCores){
                	this.numCores = logs[logIndex].getNumCores();

                    this.lowestTemps = new int[numCores];
                    this.highestTemps =  new int[numCores];
                    
                    for(int d=0; d<this.numCores;d++){
                        this.lowestTemps[d] = 1000;
                        this.highestTemps[d] = 0;
                    }
                    
                    getCores=false;
                }

                //--Get data for each core
                for(int d=0; d<this.numCores;d++){
                                        
                    //--Highest temp
                    if(this.lowestTemps[d] > logs[logIndex].getLowTemp()[d] ){
                        this.lowestTemps[d] = logs[logIndex].getLowTemp()[d];
                    }
                    //--Lowest temp
                    if(this.highestTemps[d] < logs[logIndex].getHighTemp()[d] ){
                        this.highestTemps[d] = logs[logIndex].getHighTemp()[d];
                    }
                }
                logIndex++;
            }
        }
        
        return true;
    }

    /*printTemps
     * 
     * Prints all stored temperature info to the console
     */
    public void printTemps(){
        for(int d=0; d<this.numCores;d++){
            System.out.println( "Core " + d);
            System.out.println( "L" + this.lowestTemps[d] + " H" + this.highestTemps[d] + "\n");
        }
    }
    
    /*getNumLogs
     * 
     * Returns highest logged load
     */
    public int getNumLogs(){
        return this.numLogs;
    }
    
}
