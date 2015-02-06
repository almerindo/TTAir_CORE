package org.ttair.util;

import java.util.logging.FileHandler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

public class LoggerManager {

    private static final LoggerManager INSTANCE = new LoggerManager();
    //----------------------------------------
    private static String uriFile = "./TTAirLog.log";
    private static Logger logger = Logger.getLogger("Logger");
    private static FileHandler file;

    public static void setURIFile(String fileName) {
        LoggerManager.uriFile = fileName;
      
        try {
            file = new FileHandler(uriFile, true);
            SimpleFormatter formatter = new SimpleFormatter();
            file.setFormatter(formatter);
            logger.setUseParentHandlers(true);
            logger.addHandler(file);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }


    public static void log(Level level, String mensagem) {
        logger.log(level, mensagem);
    }



    public static LoggerManager getINSTANCE() {
        return INSTANCE;
    }

}










