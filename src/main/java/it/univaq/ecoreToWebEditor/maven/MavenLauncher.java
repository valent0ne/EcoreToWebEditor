package it.univaq.ecoreToWebEditor.maven;

import com.sun.javafx.PlatformUtil;
import it.univaq.ecoreToWebEditor.core.Main;
import it.univaq.ecoreToWebEditor.utils.Constants;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;


public class MavenLauncher {

    final private static Logger LOGGER = LoggerFactory.getLogger(MavenLauncher.class);

    private String PATH_TO_POM;


    public MavenLauncher(String pathToPom) {
        PATH_TO_POM = pathToPom;

    }

    public void run() {
        LOGGER.info(Constants.ANSI_GREEN+"[MAVEN LAUNCHER - START]"+Constants.ANSI_RESET);

        if(Main.DEBUG){
            LOGGER.debug("PATH_TO_POM: {}", PATH_TO_POM);
        }

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
                System.out.print(Constants.ANSI_YELLOW+new SimpleDateFormat("HH:mm:ss").format(new Date())+Constants.ANSI_RESET+
                                 Constants.ANSI_PURPLE+" [MVN LOG] "+Constants.ANSI_RESET+
                                 line + "\n");
            }

            proc.waitFor();


        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(Constants.ANSI_RED+"[MAVEN LAUNCHER - ABORTED]"+Constants.ANSI_RESET);
            System.exit(1);
        }
        LOGGER.info(Constants.ANSI_GREEN+"[MAVEN LAUNCHER - DONE]"+Constants.ANSI_RESET);
    }

}



