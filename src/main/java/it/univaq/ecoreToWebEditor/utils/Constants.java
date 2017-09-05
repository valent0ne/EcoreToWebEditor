package it.univaq.ecoreToWebEditor.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

/**
 * Stores and handles the costants used in the application
 */
public class Constants {
    
    //os defined line separator
    public static final String LINE_SEPARATOR = System.lineSeparator();

    //file formats
    public static final String MTL_FILE_FORMAT = ".mtl";
    public static final String EMTL_FILE_FORMAT = ".emtl";
    public static final String XTEXT_FILE_FORMAT = ".xtext";
    public static final String ECORE_FILE_FORMAT = ".ecore";
    public static final String MWE2_FILE_FORMAT = ".mwe2";

    //logger colors
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

    //sub-projects suffixes
    public static final List<String> PROJECT_SUFFIX = Arrays.asList("", ".ide", ".target", ".ui", ".web");

    //constant .mwe2 fix data
    public static final int FIX_TARGET_LINE = 29;
    public static final String FIX_CONTENT = "			referencedResource = \"platform:/resource/org.eclipse.emf.ecore/model/Ecore.genmodel\"";





}
