package it.univaq.ecoreToWebEditor.acceleo;

import it.univaq.ecoreToWebEditor.core.Main;
import it.univaq.ecoreToWebEditor.utils.Utils;
import org.apache.commons.lang3.StringUtils;
import org.eclipse.emf.common.util.BasicMonitor;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static it.univaq.ecoreToWebEditor.core.Main.DEBUG;
import static it.univaq.ecoreToWebEditor.utils.Constants.*;

public class AcceleoLauncher {

    final private static Logger LOGGER = LoggerFactory.getLogger(AcceleoLauncher.class);

    private String MTL_FILE_NAME;
    private String EMTL_FILE_NAME;
    private String XTEXT_FILE_NAME;

    private String PATH_TO_MTL_FILE;
    private String PATH_TO_EMTL_FILE;
    private String PATH_TO_ECORE_FILE;

    private String PATH_TO_MTL_FOLDER;
    private String PATH_TO_EMTL_FOLDER;
    private String PATH_TO_XTEXT_FOLDER;

    private String ENTRY_POINT;

    public static Map<String, String> DEFAULTS = new HashMap<>();

    static {
        DEFAULTS.put("MTL_FILE_NAME", "generate");
        DEFAULTS.put("EMTL_FILE_NAME", DEFAULTS.get("MTL_FILE_NAME"));
        DEFAULTS.put("PATH_TO_MTL_FOLDER", "mtl"+File.separator);
        DEFAULTS.put("PATH_TO_EMTL_FOLDER", "mtl"+File.separator);
        DEFAULTS.put("PATH_TO_XTEXT_FOLDER", "gen"+File.separator);
        DEFAULTS.put("PATH_TO_MTL_FILE", "mtl"+File.separator + "generate" + MTL_FILE_FORMAT);
        DEFAULTS.put("PATH_TO_EMTL_FILE", "mtl"+File.separator + "generate" + EMTL_FILE_FORMAT);
    }

    private String[] TEMPLATE_NAMES = {"generateElement"};


    public AcceleoLauncher(String pathToEcoreFile, String mtlFileName, String emtlFileName, String xtextFileName, String pathToMtlFolder, String pathToEmtlFolder, String pathToXtextFolder, String entryPoint) {

        MTL_FILE_NAME = (mtlFileName == null || mtlFileName.isEmpty()) ? DEFAULTS.get("MTL_FILE_NAME") : mtlFileName;
        EMTL_FILE_NAME = (emtlFileName == null || emtlFileName.isEmpty()) ? DEFAULTS.get("EMTL_FILE_NAME") : emtlFileName;

        PATH_TO_MTL_FOLDER = (pathToMtlFolder == null || pathToMtlFolder.isEmpty()) ? DEFAULTS.get("PATH_TO_MTL_FOLDER") : pathToMtlFolder;
        PATH_TO_EMTL_FOLDER = (pathToEmtlFolder == null || pathToEmtlFolder.isEmpty()) ? DEFAULTS.get("PATH_TO_EMTL_FOLDER") : pathToEmtlFolder;

        PATH_TO_MTL_FILE = PATH_TO_MTL_FOLDER + MTL_FILE_NAME + MTL_FILE_FORMAT;
        PATH_TO_EMTL_FILE = PATH_TO_EMTL_FOLDER + EMTL_FILE_NAME + EMTL_FILE_FORMAT;

        XTEXT_FILE_NAME = xtextFileName;
        PATH_TO_XTEXT_FOLDER = (pathToXtextFolder == null || pathToXtextFolder.isEmpty()) ? DEFAULTS.get("PATH_TO_XTEXT_FOLDER") : pathToXtextFolder;

        ENTRY_POINT = entryPoint;

        PATH_TO_ECORE_FILE = pathToEcoreFile;

    }

    public AcceleoLauncher(){}

    public Map<String, String> getDEFAULTS(){
        return DEFAULTS;
    }


    /**
     * compile source .mtl file to dest location
     */
    private void compile() {

        LOGGER.info("compiling {}...", PATH_TO_MTL_FILE);

        try {
            AcceleoCompiler acceleoCompiler = new AcceleoCompiler();
            acceleoCompiler.setSourceFolder(PATH_TO_MTL_FOLDER);
            acceleoCompiler.setOutputFolder(PATH_TO_EMTL_FOLDER);
            acceleoCompiler.doCompile(new BasicMonitor());
        } catch (Throwable e) {
            LOGGER.error(".mtl compiler {}", e.getMessage());
            if(DEBUG){
                e.printStackTrace();
            }
            System.exit(1);
        }

        LOGGER.info(".mtl compiled to {}", PATH_TO_EMTL_FOLDER);
    }


    /**
     * load end register .ecore metamodel
     */
    public URI ecoreLoader() {

        LOGGER.info("loading {}", PATH_TO_ECORE_FILE);

        try {
            // Create a resource set.
            ResourceSet resourceSet = new ResourceSetImpl();

            // Register the default resource factory (needed for standalone)
            resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap().put("ecore", new EcoreResourceFactoryImpl());

            // Register the package (needed for standalone)
            EcorePackage ecorePackage = EcorePackage.eINSTANCE;

            // Get the URI of the model file.
            URI fileURI = URI.createFileURI(new File(PATH_TO_ECORE_FILE).getAbsolutePath());

            // Demand load the resource for this file.
            Resource resource = resourceSet.getResource(fileURI, true);

            LOGGER.info(".ecore loaded");

            return fileURI;

        } catch (Throwable e) {
            LOGGER.error(".ecore loader {}", e.getMessage());
            if(DEBUG){
                e.printStackTrace();
            }
            System.exit(1);
        }

        return null;

    }

    /**
     * acceleo generation
     */
    public void run() {

        LOGGER.info(ANSI_GREEN+"[ACCELEO LAUNCHER - START]"+ANSI_RESET);

        if(Main.DEBUG){
            LOGGER.debug("ENTRY_POINT: {}", ENTRY_POINT);
            LOGGER.debug("PATH_TO_XTEXT_FOLDER: {}", PATH_TO_XTEXT_FOLDER);
            LOGGER.debug("PATH_TO_EMTL_FILE: {}", PATH_TO_EMTL_FILE);
        }

        compile();

        LOGGER.info("generating {} to {}, entry point: {}", XTEXT_FILE_NAME+XTEXT_FILE_FORMAT, PATH_TO_XTEXT_FOLDER, ENTRY_POINT);

        try {
            List<String> acceleoArgs = new ArrayList<>();
            acceleoArgs.add(ENTRY_POINT);
            acceleoArgs.add(StringUtils.capitalize(XTEXT_FILE_NAME)+XTEXT_FILE_FORMAT);
            acceleoArgs.add(Main.LANGUAGE_NAME);
            acceleoArgs.add(Utils.languageNameToUri(Main.LANGUAGE_NAME));
            acceleoArgs.add(StringUtils.uncapitalize(Utils.getLastSegment(Utils.languageNameToUri(Main.LANGUAGE_NAME), "/")));

            File targetFolder = new File(PATH_TO_XTEXT_FOLDER);
            if(Main.DEBUG){
                LOGGER.debug("target folder set");
            }

            URI fileUri = ecoreLoader();

            Generate generator = new Generate(fileUri, targetFolder, acceleoArgs);

            if(Main.DEBUG){
                LOGGER.debug("generator ready");
            }

            generator.setModuleFileName(PATH_TO_EMTL_FILE);

            if(Main.DEBUG){
                LOGGER.debug("path to emtl file set");
            }
            generator.setTemplateNames(TEMPLATE_NAMES);

            if(Main.DEBUG){
                LOGGER.debug("template names set");
            }

            generator.doGenerate(new BasicMonitor());
        } catch (Throwable e) {
            LOGGER.error("acceleo generation: {}", e.getMessage());
            LOGGER.error(ANSI_RED+"[ACCELEO LAUNCHER - ABORTED]"+ANSI_RESET);
            if(DEBUG){
                e.printStackTrace();
            }
            System.exit(1);
        }

        LOGGER.info(ANSI_GREEN+"[ACCELEO LAUNCHER - DONE]"+ANSI_RESET+" to {}", PATH_TO_XTEXT_FOLDER);
    }


}
