
/**=============================================================*\
 *                                                              *
 *                     ConfigHandler Class                      *
 *                                                              *
 *                      by: Shelby Jueden                       *
 *                                                              *
 *    ______________________________________________________    *
 *   |                    ConfigHandler                     |   *
 *   |------------------------------------------------------|   *
 *   | - configFile:                        File            |   *
 *   | - loadFile:                          Scanner         |   *
 *   | - filePath:                          String          |   *
 *   | - rawDump:                           String[]        |   *
 *   |------------------------------------------------------|   *
 *   | + ConfigHandler()                                    |   *
 *   | + ConfigHandler(file:String)                         |   *
 *   |                                                      |   * 
 *   | + openFile(file:String):             void            |   *
 *   | + getValueFor(needle:String):        String          |   *
 *   | - getArray()                                         |   *
 *   | + countLines(file:String):           int             |   *
 *   |                                                      |   * 
 *   |______________________________________________________|   *
 *                                                              *
 *                                                              *
\*==============================================================*/

/**
 * Imports
 */
import java.util.Scanner;
import java.io.*;



public class ConfigHandler {

    //--Field Declarations
    private File configFile;
    private Scanner loadFile;
    private int itemCount = 0;
    
    private String filePath;
    
    private String[] rawDump;
    private String[] itemLines;
    private String[][] dataArray;

    
    /**
     * No-Arg Constructor
     * @param file
     * @throws IOException
     */
    public ConfigHandler(){
        
        
    }
    
    /**1
     * Primary Constructor
     * @param file
     * @throws IOException
     */
    public ConfigHandler(String file) throws IOException{
        
        this.filePath = file;
        
        getArray(filePath);
        
    }
    
    /**openFile
     * 
     * Primary Constructor
     * @param file
     * @throws IOException
     */
    public void openFile(String file) throws IOException{
        
        this.filePath = file;
        
        getArray(filePath);
        
    }
    
    /**
     * Returns a string value for the give entry in the file as a string
     * @param needle
     * @return
     */
    public String getValueFor(String needle){
        
        String value = null;
        
        for(int c = 0;c < itemCount;c++){
            
            if( needle.trim().equalsIgnoreCase(dataArray[0][c]) ){
                
                value = dataArray[1][c];
            }
            
        }
        
        return value;
    }
    
    /**getNameAt
     * Returns a string value for the entry in the array
     * @param index
     * @return
     */
    public String getNameAt(int index){
        
        return dataArray[0][index];
    }
    
    /**getValueAt
     * Returns a string value for the entry in the array
     * @param index
     * @return
     */
    public String getValueAt(int index){
        
        return dataArray[1][index];
    }

    
    /**
     * Load file into an array
     * @param file
     * @throws IOException
     */
    private void getArray(String file) throws IOException{

        this.configFile = new File(file);
                
        loadFile = new Scanner(configFile);

        int fileLength = countLines(file);
        
        this.rawDump = new String[fileLength];
        
        for(int c=0;loadFile.hasNext();c++){

            this.rawDump[c] = loadFile.nextLine();
            if( this.rawDump[c].contains(":") ){
                itemCount++;
            }
            
        }
        
        loadFile.close();
        loadFile = new Scanner(configFile);
        
        itemLines = new String[itemCount];
        itemCount = 0;

        for(int c=0;loadFile.hasNext();c++){

            this.rawDump[c] = loadFile.nextLine();
            if( this.rawDump[c].contains(":") ){
                itemLines[itemCount] = rawDump[c];
                itemCount++;
            }
            
        }
        
        
        loadFile.close();
        
        
        dataArray = new String[2][];
        dataArray[0] = new String[itemCount];
        dataArray[1] = new String[itemCount];
        
        for(int c=0;c < itemCount;c++){

            dataArray[0][c] = itemLines[c].split(":")[0].trim();
            dataArray[1][c] = itemLines[c].split(":")[1].trim();
            
        
        }
        
        
    }

    
    /**getNumItems
     * 
     * Get the number of items stored in the file
     * 
     */
    public int getNumItems(){
       
        return itemCount;
    }

    /**File Line Counter**/
    public int countLines(String path) throws IOException{
        
        int output = 0;
        
        //--Open file.
        File file = new File(path);
        Scanner inputFile = new Scanner(file);
        
        while (inputFile.hasNext()){
        
            inputFile.nextLine();
            output++;
        }

        inputFile.close();
        
        return output;
    }

    public boolean setValueFor(String type){
        
        return true;
    }
}
