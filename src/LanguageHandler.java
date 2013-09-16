import java.io.*;

public class LanguageHandler {
    
    //--Field Declarations
    private String userDesiredLang = "none";
    private String langFolderPath;
    private String[] langFileNames;
    private boolean langPacksFound = false;
    
    private int numLangFiles = 0;
    
    private String[][][] laguagesData;
    
    /**LanguageHandler
     * 
     * Constructor for just the folder path
     * 
     * @param langFolderPath
     */
    public LanguageHandler(String inputFolderPath){

        this(inputFolderPath, "none");
    }
    
    /**LanguageHandler
     * 
     * Constructor for the folder path and user's language
     * 
     * @param langFolderPath
     */
    public LanguageHandler(String inputFolderPath, String userDesiredLang){
                
        this.userDesiredLang = userDesiredLang;
        this.langFolderPath = inputFolderPath;

        loadAvailableLanugauges();
    }
    
    
    
    /**getText
     * 
     * Gets text for the entry name specified for the users default language
     * 
     * @param entryName
     * @return
     */
    public String getText(String entryName){
        
        return this.getText(entryName, userDesiredLang);
    }
    
    /**getText
     * 
     * Gets text for the entry name specified for the language specified
     * 
     * @param entryName
     * @param language
     * @return
     */
    public String getText(String entryName, String language){

        int userLanguageIndex = 0;
        
        for(int c = 0;c < laguagesData.length; c++){
            
            for(int d = 0;d < laguagesData[c][0].length; d++){
                if( laguagesData[c][1][d].equals(language) ){
                    
                    userLanguageIndex = c;
                }
            }
            
        }

        return this.getText(entryName, userLanguageIndex);
    }
    
    /**getText
     * 
     * Gets text for the entry name specified for the users default language
     * 
     * @param entryName
     * @return
     */
    public String getText(String entryName, int languageIndex){

        String value = "";
        boolean notFound = true;
        
        for(int c = 0;c < laguagesData[languageIndex][0].length; c++){
            if( laguagesData[languageIndex][0][c].equals(entryName) ){
                notFound = false;
                value = laguagesData[languageIndex][1][c];
            }
        }
        
        if(notFound){
            
        }
        
        return value;
    }
    
    
    /**getNumFiles
     * 
     * Returns the number of language files
     * 
     * @param entryName
     * @return
     */
    public int getNumFiles(){
        
        return numLangFiles;
    }
    
    /**checkUserIndex
     * 
     * Checks if a given index is the users desired language
     * 
     * @param inputIndex
     * @return
     */
    public boolean checkUserIndex(int inputIndex){

        return this.getText("id", inputIndex).equals(userDesiredLang);
    }
    
    
    
    
    
    /**loadAvailableLanugauges
     * 
     * Loads the strings for all available languages
     * 
     */
    private void loadAvailableLanugauges(){
        
        //--Load check
        boolean notLoaded = true;
        boolean langFolderExist = false;
        boolean anyLangItemExist = false;
        
        //--Check if the lang folder exists
        File langFolder = new File(langFolderPath);
        File[] langFiles = new File[0];
        if(langFolder.exists()){
            langFiles = langFolder.listFiles();
            langFolderExist = true;
        }
        
        int numFiles = 0;
        
        //--Check if there are any lang files and get list of them
        if(langFolderExist){
            numFiles = langFiles.length;
            anyLangItemExist = true;
            langPacksFound = true;
        }
        
        String tempFileNames = "";
        
        //--Get list of lang files and index of the users language
        if(anyLangItemExist){
            for(int c = 0; c < numFiles; c++){
                if( langFiles[c].isFile() ){
                    if(langFiles[c].getName().endsWith(".lang")){
                        numLangFiles++;
                        tempFileNames = tempFileNames + langFiles[c].getName() + ":";
                    }
                }
            }
        }
        
        langFileNames = tempFileNames.split(":");
        
        laguagesData = new String[numLangFiles][2][];
        for(int c = 0; c < numLangFiles;c++){
            laguagesData[c][0] = new String[numLangFiles]; 
            laguagesData[c][1] = new String[numLangFiles];   
        }
        
        //--Create File loader object to load language packs
        ConfigHandler languageFiles;
                
        try {

            //--Load each language pack at once
            if(anyLangItemExist){
                for(int c = 0; c < numLangFiles; c++){
                    languageFiles = new ConfigHandler(langFolderPath + File.separator + langFileNames[c]);
                    
                    //--Store language data
                    laguagesData[c][0] = new String[languageFiles.getNumItems()];
                    laguagesData[c][1] = new String[languageFiles.getNumItems()];
                    
                    for(int d=0;d<languageFiles.getNumItems();d++){
                        laguagesData[c][0][d] = languageFiles.getNameAt(d);
                        laguagesData[c][1][d] = languageFiles.getValueAt(d);
                    }
                    
                }
                
            }
            
        } catch (IOException e) {
            e.printStackTrace();
        }
                
    }
    
}
