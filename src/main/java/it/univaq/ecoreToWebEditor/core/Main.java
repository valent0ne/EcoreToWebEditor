package it.univaq.ecoreToWebEditor.core;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import it.univaq.ecoreToWebEditor.acceleo.AcceleoLauncher;
import it.univaq.ecoreToWebEditor.maven.MavenLauncher;
import it.univaq.ecoreToWebEditor.utils.Constants;
import it.univaq.ecoreToWebEditor.utils.Utils;
import it.univaq.ecoreToWebEditor.xtext.XtextProjectBuilder;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;

import static it.univaq.ecoreToWebEditor.utils.Utils.clean;
import static it.univaq.ecoreToWebEditor.utils.Utils.eclipsify;
import static it.univaq.ecoreToWebEditor.utils.Utils.fixMwe2;


public class Main {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);

    //cli params

    @Parameter(names = {"--debug", "-d"}, description = "debug mode")
    public static boolean DEBUG = false;

    @Parameter(names = {"--clean", "-c"}, description = "clean target folder before starting")
    public static boolean CLEAN = false;

    @Parameter(names = {"--eclypsify", "-efy"}, description = "eclipsify the generated project")
    public static boolean ECLIPSIFY = false;

    @Parameter(names = {"--nobuild", "-nb"}, description = "don't run maven build")
    public static boolean NOBUILD = false;

    //path to the output folder, default "./out"
    @Parameter(names = {"--out", "-o"}, description = "path to output folder")
    public static String PATH_TO_OUT_FOLDER = "."+File.separator+"out";

    //path to .ecore metamodel
    @Parameter(names = {"--ecore", "-e"}, required = true, description = "path to .ecore metamodel file")
    private static String PATH_TO_ECORE_FILE;

    //name of entry point EClass
    @Parameter(names = {"--entrypoint", "-ep"}, required = true, description = "name of entry point EClass")
    private static String ENTRY_POINT;

    //basename of xtext project
    @Parameter(names = {"--projectname", "-pn"}, description = "basename of xtext project")
    public static String BASE_NAME;

    //file format name of xtext dsl language
    @Parameter(names = {"--formatname", "-fn"}, description = "file format name of xtext dsl language")
    public static String FORMAT_NAME;

    //path to .mtl file
    @Parameter(names = {"--mtl", "-mtl"}, description = "path to .mtl file")
    private static String PATH_TO_MTL_FILE = AcceleoLauncher.DEFAULTS.get("PATH_TO_MTL_FILE");

    //path to .emtl file
    @Parameter(names = {"--emtl", "-emtl"}, description = "path to .emtl file")
    private static String PATH_TO_EMTL_FILE = AcceleoLauncher.DEFAULTS.get("PATH_TO_EMTL_FILE");

    //path to generated .xtext file
    @Parameter(names = {"--xtext", "-xtext"}, description = "path to generated .xtext file (suggest to leave default value)")
    private static String PATH_TO_XTEXT_FILE;


    private static String MTL_FILE_NAME;
    private static String EMTL_FILE_NAME;
    private static String PATH_TO_MTL_FOLDER;
    private static String PATH_TO_EMTL_FOLDER;
    private static String PATH_TO_POM;

    public static String PATH_TO_XTEXT_FOLDER;
    public static String XTEXT_FILE_NAME;
    public static String LANGUAGE_NAME;



    public static void main( String[] args ) {

        Main main = new Main();
        JCommander jc = JCommander.newBuilder()
                .addObject(main)
                .build();

        jc.setProgramName("java -jar ecoreToWebEditor.jar");

        try{
            jc.parse(args);

            //variables check

            if(DEBUG){
                LOGGER.debug("PATH_TO_ECORE_FILE: {}", PATH_TO_ECORE_FILE);
            }


            BASE_NAME = (BASE_NAME == null || BASE_NAME.isEmpty()) ? BASE_NAME = "org.xtext."+Utils.getLastSegment(PATH_TO_ECORE_FILE, File.separator).substring(0, Utils.getLastSegment(PATH_TO_ECORE_FILE, File.separator).length()-Constants.ECORE_FILE_FORMAT.length()).toLowerCase()+"Dsl" : BASE_NAME;
            FORMAT_NAME = (FORMAT_NAME == null || FORMAT_NAME.isEmpty()) ? Utils.getLastSegment(BASE_NAME, "\\.").toLowerCase() : FORMAT_NAME;

            PATH_TO_XTEXT_FILE = (PATH_TO_XTEXT_FILE == null || PATH_TO_XTEXT_FILE.isEmpty()) ? PATH_TO_OUT_FOLDER+
                                                                                                File.separator+
                                                                                                BASE_NAME+
                                                                                                File.separator+
                                                                                                BASE_NAME+
                                                                                                ".parent"+
                                                                                                File.separator+
                                                                                                BASE_NAME+
                                                                                                File.separator+
                                                                                                "src"+
                                                                                                File.separator+
                                                                                                Utils.serializeName(BASE_NAME)+
                                                                                                File.separator+
                                                                                                StringUtils.capitalize(Utils.getLastSegment(BASE_NAME, "\\."))+
                                                                                                Constants.XTEXT_FILE_FORMAT
                                                                                                : PATH_TO_XTEXT_FILE;

            MTL_FILE_NAME = Utils.getLastSegment(PATH_TO_MTL_FILE, File.separator).substring(0, Utils.getLastSegment(PATH_TO_MTL_FILE, File.separator).length()-Constants.MTL_FILE_FORMAT.length());
            EMTL_FILE_NAME = Utils.getLastSegment(PATH_TO_EMTL_FILE, File.separator).substring(0, Utils.getLastSegment(PATH_TO_EMTL_FILE, File.separator).length()-Constants.EMTL_FILE_FORMAT.length());
            XTEXT_FILE_NAME = Utils.getLastSegment(BASE_NAME, "\\.");
            PATH_TO_MTL_FOLDER = PATH_TO_MTL_FILE.substring(0, PATH_TO_MTL_FILE.length()-(MTL_FILE_NAME.length()+Constants.MTL_FILE_FORMAT.length()));
            PATH_TO_EMTL_FOLDER = PATH_TO_EMTL_FILE.substring(0, PATH_TO_EMTL_FILE.length()-(EMTL_FILE_NAME.length()+Constants.EMTL_FILE_FORMAT.length()));
            PATH_TO_XTEXT_FOLDER = PATH_TO_XTEXT_FILE.substring(0, PATH_TO_XTEXT_FILE.length()-(XTEXT_FILE_NAME.length()+Constants.XTEXT_FILE_FORMAT.length()));
            PATH_TO_POM = PATH_TO_OUT_FOLDER+File.separator+BASE_NAME+File.separator+BASE_NAME+".parent"+File.separator+"pom.xml";
            LANGUAGE_NAME = BASE_NAME+"."+StringUtils.capitalize(Utils.getLastSegment(Main.BASE_NAME, "\\."));

            if(Main.DEBUG){
                LOGGER.debug(Constants.ANSI_GREEN+"[DATA RECAP]"+Constants.ANSI_RESET);

                LOGGER.debug("BASE_NAME: {}",BASE_NAME);
                LOGGER.debug("FORMAT_NAME: {}",FORMAT_NAME);
                LOGGER.debug("MTL_FILE_NAME: {}",MTL_FILE_NAME);
                LOGGER.debug("EMTL_FILE_NAME: {}",EMTL_FILE_NAME);
                LOGGER.debug("LANGUAGE_NAME: {}",LANGUAGE_NAME);

                LOGGER.debug("PATH_TO_MTL_FOLDER: {}",PATH_TO_MTL_FOLDER);
                LOGGER.debug("PATH_TO_EMTL_FOLDER: {}",PATH_TO_EMTL_FOLDER);
                LOGGER.debug("PATH_TO_XTEXT_FOLDER: {}",PATH_TO_XTEXT_FOLDER);
                LOGGER.debug("PATH_TO_MTL_FILE: {}",PATH_TO_MTL_FILE);
                LOGGER.debug("PATH_TO_EMTL_FILE: {}",PATH_TO_EMTL_FILE);
                LOGGER.debug("PATH_TO_ECORE_FILE: {}", PATH_TO_ECORE_FILE);
                LOGGER.debug("PATH_TO_OUT_FOLDER: {}",PATH_TO_OUT_FOLDER);
                LOGGER.debug("PATH_TO_XTEXT_FILE: {}",PATH_TO_XTEXT_FILE);

                LOGGER.debug("PATH_TO_POM: {}", PATH_TO_POM);
                LOGGER.debug("ENTRY_POINT: {}", ENTRY_POINT);
            }

        }catch (Exception e){
            //if there is something wrong print the cli usage instructions
            jc.usage();

            if(DEBUG){
                LOGGER.error(e.getMessage());
                e.printStackTrace();
            }

            System.exit(1);
        }

        main.run();

    }

    private void run(){

        LOGGER.info(Constants.ANSI_GREEN+"[STARTING PROCESS]"+Constants.ANSI_RESET);

    
        if(CLEAN){
            clean();
        }
        //create xtext project
        new XtextProjectBuilder().run();

        //deploy .mwe2 fix
        fixMwe2();

        //run acceleo
        new AcceleoLauncher(PATH_TO_ECORE_FILE,
                            MTL_FILE_NAME,
                            EMTL_FILE_NAME,
                            XTEXT_FILE_NAME,
                            PATH_TO_MTL_FOLDER,
                            PATH_TO_EMTL_FOLDER,
                            PATH_TO_XTEXT_FOLDER,
                            ENTRY_POINT).run();

        //skip build generated xtext project (if requested)
        if(!NOBUILD){
            new MavenLauncher(PATH_TO_POM).run();
        }

        //apply eclipse files if requested
        if(ECLIPSIFY){
            eclipsify();
        }

        LOGGER.info(Constants.ANSI_GREEN+"[PROCESS COMPLETED]"+Constants.ANSI_RESET);

    }
}
