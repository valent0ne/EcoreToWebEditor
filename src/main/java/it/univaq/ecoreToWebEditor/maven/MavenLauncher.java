package it.univaq.ecoreToWebEditor.maven;

import com.sun.javafx.PlatformUtil;
import it.univaq.ecoreToWebEditor.core.Main;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

import static it.univaq.ecoreToWebEditor.core.Main.DEBUG;
import static it.univaq.ecoreToWebEditor.utils.Constants.*;


public class MavenLauncher {

    final private static Logger LOGGER = LoggerFactory.getLogger(MavenLauncher.class);

    private String PATH_TO_POM;


    public MavenLauncher(String pathToPom) {
        PATH_TO_POM = pathToPom;

    }

    public void run() {
        LOGGER.info(ANSI_GREEN+"[MAVEN LAUNCHER - START]"+ANSI_RESET);

        if(Main.DEBUG){
            LOGGER.debug("PATH_TO_POM: {}", PATH_TO_POM);
        }

        try {

            String command = "";

            //adjust generate command if windows is detected
            if (!PlatformUtil.isWindows()) {
                command = "mvn -f " + PATH_TO_POM + " clean install";
            } else {
                LOGGER.info("windows detected: adjusting command...");
                command = "cmd /c mvn -f " + PATH_TO_POM + " clean install";
            }

            LOGGER.info("running: {}", command);

            //create process and run the command
            Process proc = Runtime.getRuntime().exec(command);

            // Read the output
            BufferedReader reader = new BufferedReader(new InputStreamReader(proc.getInputStream()));

            String line = "";
            while ((line = reader.readLine()) != null) {
                System.out.print(ANSI_YELLOW+new SimpleDateFormat("HH:mm:ss").format(new Date())+ANSI_RESET+
                                 ANSI_PURPLE+" [MVN LOG] "+ANSI_RESET+
                                 line + "\n");
            }

            //wait for process to exit
            proc.waitFor();


        } catch (Throwable e) {
            LOGGER.error(e.getMessage());
            LOGGER.error(ANSI_RED+"[MAVEN LAUNCHER - ABORTED]"+ANSI_RESET);
            if(DEBUG){
                e.printStackTrace();
            }
            System.exit(1);
        }
        LOGGER.info(ANSI_GREEN+"[MAVEN LAUNCHER - DONE]"+ANSI_RESET);
    }

}



