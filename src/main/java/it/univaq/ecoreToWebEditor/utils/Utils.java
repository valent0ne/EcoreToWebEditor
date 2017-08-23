package it.univaq.ecoreToWebEditor.utils;

import it.univaq.ecoreToWebEditor.core.Main;
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
    public static void fileCopy(String source, String dest){

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
    public static String getLastSegment(String s, String splitter){
        if(Main.DEBUG){
            LOGGER.debug("getLastSegment: s={}, splitter={}",s, splitter);
        }

        if(splitter.equals("\\")){
            splitter = "\\\\";
        }

        try{
            String[] aux = s.split(splitter);
            String res =  aux[aux.length-1];
            if(Main.DEBUG){
                LOGGER.debug("getLastSegment: res={}",res);
            }
            return res;
        }catch (Exception e){
            LOGGER.error("getLastSegment: {}",e.getMessage());
            return "";
        }

    }

    /**
     * pulizia ambiente
     * @param paths array of file paths to delete
     */
    public static void clean(List<String> paths){

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
    public static void separator(){
        System.out.println("");
    }

    /**
     * checks and fix .ecore file path
     * @param PATH_TO_ECORE_FILE path to .ecore file
     * @return fixed PATH_TO_ECORE_FILE
     */
    public static String checkEcore(String PATH_TO_ECORE_FILE){
        if(Main.DEBUG){
            LOGGER.debug("checkEcore: input path={}",PATH_TO_ECORE_FILE);
        }
        if(PATH_TO_ECORE_FILE.length()<Constants.ECORE_FILE_FORMAT.length() || !(PATH_TO_ECORE_FILE.substring(PATH_TO_ECORE_FILE.length()-Constants.ECORE_FILE_FORMAT.length()).equals(Constants.ECORE_FILE_FORMAT))){
            PATH_TO_ECORE_FILE = PATH_TO_ECORE_FILE.concat(Constants.ECORE_FILE_FORMAT);
        }
        if(Main.DEBUG){
            LOGGER.debug("checkEcore: output path={}",PATH_TO_ECORE_FILE);
        }
        return PATH_TO_ECORE_FILE;
    }


    /**
     * return a serialized version of the project name
     * ex. name.of.project returns name/of/project
     */
    public static String serializeName(String s){
        if(Main.DEBUG){
            LOGGER.debug("serializeName: s={}",s);
        }
        String res = s.replace(".", File.separator);
        if(Main.DEBUG){
            LOGGER.debug("serializeName: res={}",res);
        }
        return res;
    }




}
