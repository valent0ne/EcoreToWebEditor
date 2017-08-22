package it.univaq.ecoreToWebEditor.maven;

import com.sun.javafx.PlatformUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;


public class MavenLauncher {

    final private static Logger LOGGER = LoggerFactory.getLogger(MavenLauncher.class);

    private String PATH_TO_POM;


    public MavenLauncher(String pathToPom) {
        PATH_TO_POM = (pathToPom == null || pathToPom.isEmpty() || (pathToPom.substring(pathToPom.length() - 8, pathToPom.length()).equals("/pom.xml"))) ? "" : pathToPom;

        LOGGER.info("[MAVEN LAUNCHER - DATA RECAP]");
        LOGGER.info("PATH_TO_POM: {}", PATH_TO_POM);
    }

    public void run() {
        LOGGER.info("[MAVEN LAUNCHER - START]");

        try {

            String command = "";

            if (!PlatformUtil.isWindows()) {
                command = "mvn -f " + PATH_TO_POM + " clean install";
            } else {
                LOGGER.info("windows detected: adjusting command...");
                command = "cmd /c mvn -f " + PATH_TO_POM + " clean install";
            }

            LOGGER.info("running: {}", command);

            Process proc = Runtime.getRuntime().exec(command);

            // Read the output

            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.print(line + "\n");
            }

            proc.waitFor();


        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            LOGGER.error("[MAVEN LAUNCHER - ABORTED]");
            System.exit(1);
        }
        LOGGER.info("[MAVEN LAUNCHER - DONE]");
    }

}



