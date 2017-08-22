package it.univaq.ecoreToWebEditor.acceleo;

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
import java.util.List;
import java.util.Map;

import static it.univaq.ecoreToWebEditor.utils.Constants.EMTL_FILE_FORMAT;
import static it.univaq.ecoreToWebEditor.utils.Constants.MTL_FILE_FORMAT;

class Engine {

    final private static Logger LOGGER = LoggerFactory.getLogger(Engine.class);

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

    private static Map<String, String> DEFAULTS;

    static {
        DEFAULTS.put("MTL_FILE_NAME", "generate");
        DEFAULTS.put("EMTL_FILE_NAME", DEFAULTS.get("MTL_FILE_NAME"));
        DEFAULTS.put("PATH_TO_MTL_FOLDER", "mtl/");
        DEFAULTS.put("PATH_TO_EMTL_FOLDER", DEFAULTS.get("PATH_TO_MTL_FOLDER"));
        DEFAULTS.put("PATH_TO_XTEXT_FOLDER", "gen/");
        DEFAULTS.put("PATH_TO_MTL_FILE", DEFAULTS.get("PATH_TO_MTL_FOLDER") + DEFAULTS.get("MTL_FILE_NAME") + MTL_FILE_FORMAT);
        DEFAULTS.put("PATH_TO_EMTL_FILE", DEFAULTS.get("PATH_TO_EMTL_FOLDER") + DEFAULTS.get("MTL_EFILE_NAME") + EMTL_FILE_FORMAT);
    }

    private String[] TEMPLATE_NAMES = {"generateElement"};


    Engine(String pathToEcoreFile, String mtlFileName, String emtlFileName, String xtextFileName, String pathToMtlFolder, String pathToEmtlFolder, String pathToXtextFolder, String entryPoint) {

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


    /**
     * compile source .mtl file to dest location
     */
    public void compile() {

        LOGGER.info("compiling {}...", PATH_TO_MTL_FILE);

        try {
            AcceleoCompiler acceleoCompiler = new AcceleoCompiler();
            acceleoCompiler.setSourceFolder(PATH_TO_MTL_FOLDER);
            acceleoCompiler.setOutputFolder(PATH_TO_EMTL_FOLDER);
            acceleoCompiler.doCompile(new BasicMonitor());
        } catch (Throwable e) {
            LOGGER.error(".mtl compiler {}", e.getMessage());
            e.printStackTrace();
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
            e.printStackTrace();
            System.exit(1);
        }

        return null;

    }

    /**
     * acceleo generation
     */
    public void generate() {


        LOGGER.info("generating {} to {}, entry point: {}", XTEXT_FILE_NAME, PATH_TO_XTEXT_FOLDER, ENTRY_POINT);

        try {
            List<String> acceleoArgs = new ArrayList<>();
            acceleoArgs.add(ENTRY_POINT);
            acceleoArgs.add(XTEXT_FILE_NAME);

            File targetFolder = new File(PATH_TO_XTEXT_FOLDER);

            Generate generator = new Generate(ecoreLoader(), targetFolder, acceleoArgs);

            generator.setModuleFileName(PATH_TO_EMTL_FILE);
            generator.setTemplateNames(TEMPLATE_NAMES);

            generator.doGenerate(new BasicMonitor());
        } catch (Throwable e) {
            LOGGER.error("acceleo generation: {}", e.getMessage());
            e.printStackTrace();
            System.exit(1);
        }

        LOGGER.info("generation completed to {}", PATH_TO_XTEXT_FOLDER);
    }


}
