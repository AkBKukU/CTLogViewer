

/** Imports **/
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import javax.swing.*;
import javax.swing.event.*;


public class LogViewGui extends JFrame{

    //--Field Declarations
    
    //--Panels
    private JPanel masterPanel;
    private JPanel overviewPanel;
    private JPanel graphPanel;
    

    //--Gui Language items
    private LanguageHandler langHandler;
    private String userLanguage;
    
    
    /**LogViewGui
     * 
     * Defines and displays the window
     * 
     */
    public LogViewGui(){
        
        
        //--Set title
        super("CT Log Viewer");

        //--Load text
        loadLanguage();
        
        //--Build GUI
        buildOverviewPanel();
        
        //--Field Declarations
        final int   DEFAULT_WIN_X = 600,
                    DEFAULT_WIN_Y = 800;
        
        //--Set Values
        setSize(DEFAULT_WIN_X,DEFAULT_WIN_Y);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        //--Add Components
        add(overviewPanel);
        
        //--Show Window
        setVisible(true);
        
        
    }
    
    
    /*buildOverviewPanel
     * 
     * Creates the panel for the overview area of the program
     * 
     */
    private void buildOverviewPanel(){
        
        overviewPanel = new JPanel();
        //--Add padding
        overviewPanel.setBorder(BorderFactory.createEmptyBorder(3,0,3,0) );
        overviewPanel.setLayout( new BorderLayout() );
        
        overviewPanel.add( new JLabel( langHandler.getText("overviewTitle") ) );
    }
    
    
    /**loadLanguage
     * "/"
     * Loads the strings for the selected language
     * 
     */
    private void loadLanguage(){
        
        boolean settingsExist = false;
        
        //--Get starting directory
        String stDir = System.getProperty("user.dir");
        
        //--Check settings file for preferred language
        File settingsFile = new File(stDir + File.separator + "settings.cfg");
        ConfigHandler settingsConfig;
        
        //--Load Settings
        if(settingsFile.exists()){
            try {
                settingsConfig = new ConfigHandler(settingsFile.getPath());
                userLanguage = settingsConfig.getValueFor("lang");
                settingsExist = true;
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            
        }
        
        //--Load Language handler
        langHandler = new LanguageHandler(stDir + File.separator + "lang", userLanguage);
        
    }
}
