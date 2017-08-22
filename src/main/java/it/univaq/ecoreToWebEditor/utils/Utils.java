package it.univaq.ecoreToWebEditor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.List;

public class Utils {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);


    /**
     * copyies source file in dest location
     * @param source source file full path
     * @param dest destination full path
     */
    static void fileCopy(String source, String dest){

        LOGGER.info("copying {} to {}", source, dest);

        File file = new File(source);
        try{
            Files.copy(file.toPath(), new File(dest).toPath(), StandardCopyOption.REPLACE_EXISTING);
        }catch(IOException e){
            LOGGER.error("Files.copy: {}", e.getMessage());
            System.exit(1);
        }

        LOGGER.info("file copied");

    }

    /**
     * returns the last segment after "/" in a string path
     */
    static String getLastSegment(String s){
        try{
            String[] aux = s.split("/");
            String res =  aux[aux.length-1];
            return res;
        }catch (Exception e){
            return "";
        }

    }

    /**
     * pulizia ambiente
     * @param paths array of file paths to delete
     */
    static void clean(List<String> paths){

        for(String p : paths){
            try {
                File file = new File(p);
                if(file.delete()) {
                    LOGGER.info("{} deleted", p);
                }else {
                    LOGGER.warn("can't delete {}, proceeding anyway...", p);
                }
            }catch(Exception e) {
                LOGGER.warn("exception deleting {}, proceeding anyway...",e.getMessage());
            }
        }
    }

    /**
     * prints an os defined line separator
     */
    static void separator(){
        System.out.println("");
    }
}
