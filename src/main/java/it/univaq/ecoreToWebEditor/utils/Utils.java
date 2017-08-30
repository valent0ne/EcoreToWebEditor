package it.univaq.ecoreToWebEditor.utils;

import it.univaq.ecoreToWebEditor.core.Main;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import static it.univaq.ecoreToWebEditor.core.Main.*;
import static it.univaq.ecoreToWebEditor.utils.Constants.*;

public class Utils {

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);


    /**
     * copyies source file in dest location
     *
     * @param source source file full path
     * @param dest   destination full path
     */
    public static void fileCopy(String source, String dest) {

        LOGGER.info("fileCopy: copying {} to {}", source, dest);

        File file = new File(source);
        try {
            Files.copy(file.toPath(), new File(dest).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            LOGGER.error("fileCopy: {}", e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            System.exit(1);
        }

        LOGGER.info("fileCopy: file copied");

    }

    /**
     * returns the last segment after "/" in a string path
     */
    public static String getLastSegment(String s, String splitter) {
        if (DEBUG) {
            LOGGER.debug("getLastSegment: s={}, splitter={}", s, splitter);
        }

        if (splitter.equals("\\")) {
            splitter = "\\\\";
        }

        try {
            String[] aux = s.split(splitter);
            String res = aux[aux.length - 1];
            if (DEBUG) {
                LOGGER.debug("getLastSegment: res={}", res);
            }
            return res;
        } catch (Exception e) {
            LOGGER.error("getLastSegment: {}", e.getMessage());
            return "";
        }

    }

    /**
     * workaround for issue
     * https://www.eclipse.org/forums/index.php/m/1771804
     * https://github.com/eclipse/xtext-core/issues/41
     */

    public static void fixMwe2() {
        LOGGER.info("deploying .mwe2 fix");
        String path = PATH_TO_XTEXT_FOLDER+File.separator+"Generate"+XTEXT_FILE_NAME+MWE2_FILE_FORMAT;
        if(DEBUG){
            LOGGER.debug("path to .mwe2 file: {}", path);
            LOGGER.debug("new line position: {}", FIX_TARGET_LINE);
            LOGGER.debug("new line content: {}", FIX_CONTENT);
        }
        try{
            List<String> lines = Files.readAllLines(Paths.get(path), StandardCharsets.UTF_8);
            lines.add(FIX_TARGET_LINE, FIX_CONTENT);
            Files.write(Paths.get(path), lines, StandardCharsets.UTF_8);
            LOGGER.info(".mwe2 fixed");
        }catch (Exception e){
            LOGGER.error("fixMwe2: {}", e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            System.exit(1);
        }

    }

    /**
     * deletes target folder and its content
     */
    public static void clean() {

        File target = new File(PATH_TO_OUT_FOLDER+File.separator+BASE_NAME);
        LOGGER.info("clean: deleting {} and its content...", target);

        try{
            FileUtils.deleteDirectory(target);
        }catch (Exception e){
            LOGGER.warn("clean: can't clean {}, proceeding anyway...",target );
        }

        LOGGER.info("clean: {} deleted", target);
    }

    /**
     * prints an os defined line separator
     */
    public static void separator() {
        System.out.println("");
    }

    /**
     * checks and fix .ecore file path
     *
     * @param PATH_TO_ECORE_FILE path to .ecore file
     * @return fixed PATH_TO_ECORE_FILE
     */
    public static String checkEcore(String PATH_TO_ECORE_FILE) {
        if (DEBUG) {
            LOGGER.debug("checkEcore: input path={}", PATH_TO_ECORE_FILE);
        }
        if (PATH_TO_ECORE_FILE.length() < ECORE_FILE_FORMAT.length() || !(PATH_TO_ECORE_FILE.substring(PATH_TO_ECORE_FILE.length() - ECORE_FILE_FORMAT.length()).equals(ECORE_FILE_FORMAT))) {
            PATH_TO_ECORE_FILE = PATH_TO_ECORE_FILE.concat(ECORE_FILE_FORMAT);
        }
        if (DEBUG) {
            LOGGER.debug("checkEcore: output path={}", PATH_TO_ECORE_FILE);
        }
        return PATH_TO_ECORE_FILE;
    }


    /**
     * return a serialized version of the project name
     * ex. name.of.project returns name/of/project
     */
    public static String serializeName(String s) {
        if (DEBUG) {
            LOGGER.debug("serializeName: s={}", s);
        }
        String res = s.replace(".", File.separator);
        if (DEBUG) {
            LOGGER.debug("serializeName: res={}", res);
        }
        return res;
    }

    /**
     * transform a language name to uri format
     * ex. org.xtext.webdsl.WebDsl to http://www.xtext.org/webdsl/WebDsl
     *
     * @param s language name
     * @return uri version on language name
     */
    public static String languageNameToUri(String s) {
        if (DEBUG) {
            LOGGER.debug("languageNameToUri: input={}", s);
        }
        String aux[] = s.split("\\.");
        if (DEBUG) {
            LOGGER.debug("languageNameToUri: # of items={}", aux.length);
        }
        String res = "http://www." + aux[1] + "." + aux[0] + "/";
        for (int i = 2; i < aux.length - 1; i++) {
            res += aux[i] + "/";
        }
        res = res.substring(0, res.length() - 1);
        if (DEBUG) {
            LOGGER.debug("languageNameToUri: output={}", res);
        }
        return res;
    }

    /**
     * add eclipse specific configuration files to easily import the generated project inside eclipse
     */
    public static void eclipsify() {

        LOGGER.info(ANSI_GREEN+"[ECLIPSIFY - START]"+ANSI_RESET);

        LOGGER.info("targets loaded");
        List<String> targets = getTargets();

        LOGGER.info("handleProjectFile: loading and editing .classpath and .project files");

        handleClasspathFile(targets);
        handleProjectFile(targets);

        LOGGER.info(ANSI_GREEN+"[ECLIPSIFY - DONE]"+ANSI_RESET);


    }

    //---------------------------------------------- private methods -------------------------------------------------//

    /**
     * support method for eclipsify, handles .classpath files
     *
     * @param targets list of target paths
     */
    private static void handleClasspathFile(List<String> targets) {


        try {

            InputStream cp_web = getFile(File.separator+".classpath_web");


            for (String t : targets) {
                //get the .classpath file and copy it to target directories
                InputStream cp = getFile(File.separator+".classpath");

                File target = new File(t + File.separator + ".classpath");

                if (DEBUG) {
                    LOGGER.debug("handleClasspath: target is {}", target);
                }

                //if cp is null an exception is thrown before reaching this point
                assert cp_web != null;
                assert cp != null;

                if(t.substring(t.length()-3, t.length()).equals("web")){
                    if(DEBUG){
                        LOGGER.debug("handleclasspath: .web subproject detected");
                    }
                    FileUtils.copyInputStreamToFile(cp_web, target);
                }else{
                    FileUtils.copyInputStreamToFile(cp, target);
                }


                if (DEBUG) {
                    LOGGER.debug("handleClasspath: .classpath copied to {}", target);
                }
            }
        } catch (Exception e) {
            LOGGER.error("handleClasspath: {}", e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }

    /**
     * support method for eclipsify, handles .project files
     *
     * @param targets list of target paths
     */
    private static void handleProjectFile(List<String> targets) {


        try {

            InputStream prj_web = getFile(File.separator+".project_web");


            for (String t : targets) {

                InputStream prj = getFile(File.separator+".project");

                String path = t + File.separator + ".project";
                File target = new File(path);

                if (DEBUG) {
                    LOGGER.debug("handleProjectFile: target is {}", target);
                }

                //if prj is null an exception is thrown before reaching this point
                assert prj != null;
                assert prj_web != null;

                if(t.substring(t.length()-3, t.length()).equals("web")){
                    if(DEBUG){
                        LOGGER.debug("handleProjectFile: .web subproject detected");
                    }
                    FileUtils.copyInputStreamToFile(prj_web, target);
                }else{
                    FileUtils.copyInputStreamToFile(prj, target);
                }

                if (DEBUG) {
                    LOGGER.debug("handleProjectFile: .project copied to {}", target);
                }


                if (DEBUG) {
                    LOGGER.debug("handleProjectFile: loading {}", path);
                }
                DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
                Document doc = docBuilder.parse(target);

                //get "name" node
                Node projectNameNode = doc.getElementsByTagName("name").item(0);

                String data = getLastSegment(t, File.separator);
                if (DEBUG) {
                    LOGGER.debug("handleProjectFile: updating <name> with {}", data);
                }
                //set value inside "name" node
                projectNameNode.setTextContent(data);

                if (DEBUG) {
                    LOGGER.debug("handleProjectFile: writing changes");
                }
                //write back edits to the file
                TransformerFactory transformerFactory = TransformerFactory.newInstance();
                Transformer transformer = transformerFactory.newTransformer();
                DOMSource source = new DOMSource(doc);
                StreamResult result = new StreamResult(new File(path));
                transformer.transform(source, result);

            }
        } catch (Exception e) {
            LOGGER.error("handleClasspath: {}", e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            System.exit(1);
        }
    }


    /**
     * support method for eclipsify, return a list of targets to where eclipse files will be copied
     */
    private static List<String> getTargets() {
        List<String> targets = new ArrayList<>();
        for (String s : PROJECT_SUFFIX) {
            String target = PATH_TO_OUT_FOLDER +
                    File.separator +
                    BASE_NAME +
                    File.separator +
                    BASE_NAME +
                    ".parent" +
                    File.separator +
                    BASE_NAME +
                    s;
            targets.add(target);
            if (DEBUG) {
                LOGGER.debug("getTargets() added target: {}", target);
            }
        }
        return targets;
    }

    /**
     * retrieve file inside the resources folder
     *
     * @param fileName name of the file to retrieve
     * @return file as inputstream
     */
    private static InputStream getFile(String fileName) {

        InputStream stream = Main.class.getResourceAsStream(fileName);

        try {

            if (stream == null) {
                throw new Exception("cannot find file " + fileName);
            }

            return stream;

        } catch (Exception e) {
            LOGGER.error("getFile: {}", e.getMessage());
            if (DEBUG) {
                e.printStackTrace();
            }
            System.exit(1);
        }

        return null;
    }


}
