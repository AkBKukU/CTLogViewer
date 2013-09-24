import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;


public class Log {
    
    //--Fields
    
    //--General Info
    private String fileName = "";
    private String startTime = "";
    private String endTime = "";
    private String cpuId = "";
    private String cpuName = "";
    private String cpuPlatform = "";
    private String cpuRevision = "";
    private String cpuLithography = "";
    private int numCores = 0;
    
    //--Logged Data
    private int[][] temps;
    private int[][] load;
    private int[] lowTemp;
    private int[] highTemp;
    private int[] lowLoad;
    private int[] highLoad;
    private Float[] lowSpeed;
    private Float[] highSpeed;
    private Float[][] speed;
    private String[] time;
    
    //--File
    private String filePath;
    private String[] rawDump;
    
    //--Other
    private static final int DATA_START = 9; //--The line that Core Temp actually starts putting the raw data in. 
    
    /*constructor
     * 
     * Initializes all data from the log file  
     * 
     */
    public Log(String filePath) throws IOException{
        this.filePath = filePath;
        fileToArray();
        isValid();
        if(rawDump.length > 9){
            getGeneralInfo();
            getData();
        }
    }
    
    /*fileToArray
     * 
     * Loads log file into an array
     */
    private void fileToArray() throws IOException{
        
        
        //--Create file of log
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(this.filePath)));
        //--Get number of lines
        int fileLength = this.countLines();
        //--Initialize file to put lines in
        this.rawDump = new String[fileLength];
        
        //--Run through the file to put it in the array
        for(int c=0;scanner.hasNext();c++){
            this.rawDump[c] = scanner.nextLine();
        }
        scanner.close();
        
    }
    
    /*getGeneralInfo
     * 
     * Stores the general info from the log`
     */
    private boolean getGeneralInfo(){
        
        boolean success = true;

        
        //--Get Filename
        String path[] = this.filePath.split(File.separator);
        this.fileName = path[path.length-1];
        
        //--Get CPUID
        if(rawDump[0].split(",")[0].trim().equals("CPUID:") ){
            this.cpuId = rawDump[0].split(",")[1].trim();
        }else{
            success = false;
        }
        
        //--Get Processor
        if(!(rawDump[1].equals("Processor:,")) && rawDump[1].split(",")[0].trim().equals("Processor:")){
            this.cpuName = rawDump[1].split(",")[1].trim();
        }else{
            success = false;
        }
        
        //--Get Platform
        if(rawDump[2].split(",")[0].trim().equals("Platform:")){
            this.cpuPlatform = rawDump[2].split(",")[1].trim();
        }else{
            success = false;
        }
        
        //--Get Revision
        if(rawDump[3].split(",")[0].trim().equals("Revision:")){
            this.cpuRevision = rawDump[3].split(",")[1].trim();
        }else{
            success = false;
        }
        
        //--Get Lithography
        if(rawDump[4].split(",")[0].trim().equals("Lithography:")){
            this.cpuLithography = rawDump[4].split(",")[1].trim();
        }else{
            success = false;
        }
        
        //--Get start time
        this.startTime = rawDump[DATA_START].split(",")[0].trim();
        this.startTime = toISO8601(this.startTime);
        
        //--Get end time 
        for(int c=rawDump.length-1; this.endTime.equals("") ;c-- ){
            if(!(rawDump[c].equals("")) && Character.isDigit(rawDump[c].charAt(0)) ){
                this.endTime = rawDump[c].split(",")[0].trim();
            }
        }
        this.endTime = toISO8601(this.endTime);
        
        //--Get number of cores
        String tHeader[] = rawDump[8].split(",");
        this.numCores = 0; 
        for(int c=1; tHeader[c].contains("Core") ;c++ ){
            this.numCores++;
        }
        
        return success;
    }
    
    /*getData
     * 
     * Loads the tempurature data into arrays
     */
    private void getData() throws IOException{
        
        //--Get location of last entry
        int last = 0;
        for(int c=rawDump.length-1; last == 0 ;c-- ){
            if(!(rawDump[c].equals("")) && Character.isDigit(rawDump[c].charAt(0)) ){
                last = c+1;
            }
        }
        
        //--Build data arrays
        this.time = new String[last-DATA_START];
        this.temps = new int[last-DATA_START][numCores];
        this.load = new int[last-DATA_START][numCores];
        this.speed = new Float[last-DATA_START][numCores];
        this.lowTemp = new int[numCores];
        this.highTemp = new int[numCores];
        this.lowLoad = new int[numCores];
        this.highLoad = new int[numCores];
        this.lowSpeed = new Float[numCores];
        this.highSpeed = new Float[numCores];
        for(int d=0; d<this.numCores;d++){
            this.lowTemp[d] = 1000;
            this.highTemp[d] = 0;
            this.lowLoad[d] = 1000;
            this.highLoad[d] = 0;
            this.lowSpeed[d] = 10000f;
            this.highSpeed[d] = 0f;
        }
        
        
        //--Run through all entries
        for(int c=9;c < last;c++){
            
            String[] entry = rawDump[c].split(",");
            
            //--Get timestamp of entry
            this.time[c-DATA_START] = toISO8601(entry[0]);
            
            //--Get data for each core
            for(int d=0; d<this.numCores;d++){
                
                this.temps[c-DATA_START][d] = Integer.parseInt(entry[1+d]);
                this.load[c-DATA_START][d] = Integer.parseInt(entry[1+numCores+1+(d*5)+3]);
                this.speed[c-DATA_START][d] = Float.parseFloat(entry[1+numCores+1+(d*5)+4]);
                
                //--Highest temp
                if(this.lowTemp[d] > Integer.parseInt(entry[1+d]) ){
                    this.lowTemp[d] = Integer.parseInt(entry[1+d]);
                }
                //--Lowest temp
                if(this.highTemp[d] < Integer.parseInt(entry[1+d]) ){
                    this.highTemp[d] = Integer.parseInt(entry[1+d]);
                }
                
                //--Highest load
                if(this.lowLoad[d] > Integer.parseInt(entry[1+numCores+1+(d*5)+3]) ){
                    this.lowLoad[d] = Integer.parseInt(entry[1+numCores+1+(d*5)+3]);
                }
                //--Lowest load
                if(this.highLoad[d] < Integer.parseInt(entry[1+numCores+1+(d*5)+3]) ){
                    this.highLoad[d] = Integer.parseInt(entry[1+numCores+1+(d*5)+3]);
                }

                //--Highest temp
                if(this.lowSpeed[d] > Float.parseFloat(entry[1+numCores+1+(d*5)+4]) ){
                    this.lowSpeed[d] = Float.parseFloat(entry[1+numCores+1+(d*5)+4]);
                }
                //--Lowest temp
                if(this.highSpeed[d] < Float.parseFloat(entry[1+numCores+1+(d*5)+4]) ){
                    this.highSpeed[d] = Float.parseFloat(entry[1+numCores+1+(d*5)+4]);
                }
            }
        }
        
    }
    
    
    /*toISO8601
     * 
     * Converts the timestamp format that Core Temp uses to the ISO-8601 standard
     */
    private String toISO8601(String input){
        
        String output = "";
        
        if(input.length() > 8){
            output =
                "20" + input.substring(15,17) + "-" + 
                input.substring(12,14) + "-" + 
                input.substring(9,11) + "T" +
                input.substring(0,8);
        }else{
            output = input;
        }
        
        return output;
    }
    
    /*countLines
     * 
     * File Line Counter
     */
    public int countLines() throws IOException{
        
        int output = 0;
        
        //--Open file
        Scanner scanner = new Scanner(new BufferedReader(new FileReader(this.filePath)));
        
        
        while (scanner.hasNextLine()){
            
            scanner.nextLine();
            output++;
        }
        
        scanner.close();
        
        return output;
    }
    
    /*isValid
     * 
     * returns whether or not the file is a valid
     * 
     */
    public boolean isValid(){
        
        boolean validity = false;
        
        if(
            rawDump.length > 9 &&
            rawDump[0].split(",")[0].equals("CPUID:") &&
            !(rawDump[9].equals(""))
                
        ){
            validity = true;
        }
        return validity;
    }
    



    /*toString
     * 
     * Return obisValidject info
     */
    public String toString(){
        
        String output = 
            "File: " + this.fileName + "\n" +
            "From: " + this.startTime + " to " + this.endTime + "\n" +
            "CPUID: " + this.cpuId + "\n" +
            "CPU Name: " + this.cpuName + "\n" +
            "CPU Platform: " + this.cpuPlatform + "\n" +
            "CPU Revision: " + this.cpuRevision + "\n" +
            "CPU Lithography: " + this.cpuLithography + "\n" +
            "Core Count: " + this.numCores
            ;
        
        return output;
    }

    /*printTemps
     * 
     * Prints all stored temperature info to the console
     */
    public void printTemps(){

        System.out.println("Stored temperatures for log " + this.fileName);
        
        for(int c=0;c<this.time.length;c++){
            String outLine = time[c] + ": ";

            for(int d=0; d<this.numCores;d++){
                outLine = outLine + "Core "+ d + ": " + this.temps[c][d] + "°" + " Load: " + this.load[c][d] + " Speed: " + this.speed[c][d] + "MHz, ";
            }
            System.out.println(outLine);
        }

        for(int d=0; d<this.numCores;d++){
            System.out.println( "The lowest temp for Core " + d + " was " + this.lowTemp[d]);
            System.out.println( "The highest temp for Core " + d + " was " + this.highTemp[d]);
        }
    }
    
    
    
    
    
    /* Accessors
     * 
     * Use these to retrieve all the data from the object
     * 
     */


    /*getFileName
     * 
     * Returns the name of the log file
     */
    public String getFileName(){
        return this.fileName;
    }
    
    /*getStartTime
     * 
     * Returns the time in the first log entry
     */
    public String getStartTime(){
        return this.startTime;
    }
    
    /*getEndTime
     * 
     * Returns the time in the last log entry
     */
    public String getEndTime(){
        return this.endTime;
    }
    
    /*getCpuId
     * 
     * Returns the hex ID for the CPU
     */
    public String getCpuId(){
        return this.cpuId;
    }
    
    /*getCpuName
     * 
     * Returns the stored readable name for the processor
     */
    public String getCpuName(){
        return this.cpuName;
    }
    
    /*getCpuPlatform
     * 
     * Returns the platform and socket of the CPU
     */
    public String getCpuPlatform(){
        return this.cpuPlatform;
    }
    
    /*getCpuRevision
     * 
     * Returns the revision of the CPU
     */
    public String getCpuRevision(){
        return this.cpuRevision;
    }
    
    /*getCpuLithography
     * 
     * Returns the lithography or die size of the CPU
     */
    public String getCpuLithography(){
        return this.cpuLithography;
    }
    
    /*getNumCores
     * 
     * Returns the number of cores the CPU has
     */
    public int getNumCores(){
        return this.numCores;
    }
    
    
    

    /*getTime
     * 
     * Returns log times
     */
    public String[] getTime(){
        return this.time;
    }
    
    /*getTemps
     * 
     * Returns logged temperatures
     */
    public int[][] getTemps(){
        return this.temps;
    }
    
    /*getLoad
     * 
     * Returns logged loads
     */
    public int[][] getLoad(){
        return this.load;
    }
    
    /*getSpeed
     * 
     * Returns logged CPU speeds
     */
    public Float[][] getSpeed(){
        return this.speed;
    }
    
    /*getLowTemp
     * 
     * Returns lowest logged temperature
     */
    public int[] getLowTemp(){
        return this.lowTemp;
    }
    
    /*getHighTemp
     * 
     * Returns highest logged temperature
     */
    public int[] getHighTemp(){
        return this.highTemp;
    }
    
    /*getLowLoad
     * 
     * Returns lowest logged load
     */
    public int[] getLowLoad(){
        return this.lowLoad;
    }
    
    /*getHighLoad
     * 
     * Returns highest logged load
     */
    public int[] getHighLoad(){
        return this.highLoad;
    }
    
    /*getLowSpeed
     * 
     * Returns lowest logged CPU speed
     */
    public Float[] getLowSpeed(){
        return this.highSpeed;
    }
    
    /*getHighSpeed
     * 
     * Returns highest logged CPU speed
     */
    public Float[] getHighSpeed(){
        return this.highSpeed;
    }
    
}
