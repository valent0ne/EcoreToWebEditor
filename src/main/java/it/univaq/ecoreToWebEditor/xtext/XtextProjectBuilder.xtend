package it.univaq.ecoreToWebEditor.xtext

import com.google.common.base.Charsets
import java.io.File

import org.eclipse.xtext.util.XtextVersion

import org.eclipse.xtext.xtext.wizard.BuildSystem
import org.eclipse.xtext.xtext.wizard.LanguageDescriptor.FileExtensions
import org.eclipse.xtext.xtext.wizard.ProjectLayout
import org.eclipse.xtext.xtext.wizard.SourceLayout
import org.eclipse.xtext.xtext.wizard.WizardConfiguration
import org.eclipse.xtext.xtext.wizard.cli.CliProjectsCreator

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static it.univaq.ecoreToWebEditor.utils.Constants.ANSI_GREEN
import static it.univaq.ecoreToWebEditor.utils.Constants.ANSI_RED
import static it.univaq.ecoreToWebEditor.utils.Constants.ANSI_RESET
import static it.univaq.ecoreToWebEditor.core.Main.DEBUG
import static it.univaq.ecoreToWebEditor.core.Main.PATH_TO_OUT_FOLDER
import static it.univaq.ecoreToWebEditor.core.Main.BASE_NAME

import it.univaq.ecoreToWebEditor.core.Main

public class XtextProjectBuilder{



    private final String XTEXT_VERSION = "2.12.0"

    //logger
    private static final Logger LOGGER = LoggerFactory.getLogger(XtextProjectBuilder.getClass)

    new(){

    }

    def void run() {

        LOGGER.info(ANSI_GREEN+"[XTEXTPROJECTBUILDER - START]"+ANSI_RESET)
        try{
            LOGGER.info("generating project at {}", PATH_TO_OUT_FOLDER+File.separator+BASE_NAME);

            val creator = newProjectCreator
            projectConfigs.forEach [ config |
                val targetLocation = new File(Main.PATH_TO_OUT_FOLDER, config.baseName)
                targetLocation.mkdirs
                org.eclipse.xtext.util.Files.sweepFolder(targetLocation)
                config.rootLocation = targetLocation.path
                creator.createProjects(config)
            ]
        }catch(Throwable e){
            LOGGER.error(ANSI_RED+"[XTEXTPROJECTBUILDER - ABORTED]"+ANSI_RESET)
            LOGGER.error(e.getMessage());

            if(DEBUG){
                e.printStackTrace();
            }
            System.exit(1);
        }

        LOGGER.info(ANSI_GREEN+"[XTEXTPROJECTBUILDER - DONE]"+ANSI_RESET)

    }

    val projectConfigs = #[
        newProjectConfig => [
            baseName = Main.BASE_NAME
            preferredBuildSystem = BuildSystem.MAVEN
            sourceLayout = SourceLayout.PLAIN
            projectLayout = ProjectLayout.HIERARCHICAL
            runtimeProject.testProject.enabled = false //was true
            uiProject.enabled = true
            uiProject.testProject.enabled = false //was true
            ideProject.enabled = true
            webProject.enabled = true
            lineDelimiter = "\n"
        ]
        //      ,
        //        newProjectConfig => [
        //            baseName = "org.xtext.example.plainMaven"
        //            preferredBuildSystem = BuildSystem.MAVEN
        //            sourceLayout = SourceLayout.MAVEN
        //            projectLayout = ProjectLayout.HIERARCHICAL
        //            runtimeProject.testProject.enabled = true
        //            ideProject.enabled = true
        //            webProject.enabled = true
        //            lineDelimiter = "\n"
        //        ],
        //        newProjectConfig => [
        //            baseName = "org.xtext.example.gradle"
        //            preferredBuildSystem = BuildSystem.GRADLE
        //            sourceLayout = SourceLayout.MAVEN
        //            projectLayout = ProjectLayout.HIERARCHICAL
        //            runtimeProject.testProject.enabled = true
        //            ideProject.enabled = true
        //            webProject.enabled = true
        //            intellijProject.enabled = true
        //            lineDelimiter = "\n"
        //        ],
        //        newProjectConfig => [
        //            baseName = "org.xtext.example.eclipsePlugin"
        //            preferredBuildSystem = BuildSystem.NONE
        //            sourceLayout = SourceLayout.PLAIN
        //            projectLayout = ProjectLayout.FLAT
        //            runtimeProject.testProject.enabled = true
        //            ideProject.enabled = true
        //            uiProject.enabled = true
        //            uiProject.testProject.enabled = true
        //            lineDelimiter = "\n"
        //        ],
        //        newProjectConfig => [
        //            baseName = "org.xtext.example.eclipsePluginP2"
        //            preferredBuildSystem = BuildSystem.NONE
        //            sourceLayout = SourceLayout.PLAIN
        //            projectLayout = ProjectLayout.FLAT
        //            runtimeProject.testProject.enabled = true
        //            ideProject.enabled = true
        //            uiProject.enabled = true
        //            uiProject.testProject.enabled = true
        //            p2Project.enabled = true
        //            lineDelimiter = "\n"
        //        ],
        //        newProjectConfig => [
        //            baseName = "org.xtext.example.mavenTychoP2"
        //            preferredBuildSystem = BuildSystem.MAVEN
        //            sourceLayout = SourceLayout.PLAIN
        //            projectLayout = ProjectLayout.HIERARCHICAL
        //            runtimeProject.testProject.enabled = true
        //            uiProject.enabled = true
        //            uiProject.testProject.enabled = true
        //            ideProject.enabled = true
        //            webProject.enabled = true
        //            p2Project.enabled = true
        //            lineDelimiter = "\n"
        //        ],
        //        newProjectConfig => [
        //            baseName = "org.xtext.example.full"
        //            preferredBuildSystem = BuildSystem.GRADLE
        //            sourceLayout = SourceLayout.PLAIN
        //            projectLayout = ProjectLayout.HIERARCHICAL
        //            runtimeProject.testProject.enabled = true
        //            uiProject.enabled = true
        //            uiProject.testProject.enabled = true
        //            ideProject.enabled = true
        //            webProject.enabled = true
        //            intellijProject.enabled = true
        //            p2Project.enabled = true
        //            lineDelimiter = "\n"
        //        ]
    ]

    private def newProjectConfig() {
        new WizardConfiguration => [
            xtextVersion = new XtextVersion(XTEXT_VERSION)
            encoding = Charsets.UTF_8
            language => [
                name = Main.LANGUAGE_NAME //ex. "org.xtext.example.testdsl.TestDsl"
                fileExtensions = FileExtensions.fromString(Main.FORMAT_NAME)
            ]
        ]
    }

    private def newProjectCreator() {
        new CliProjectsCreator => [
            lineDelimiter = "\n"
        ]
    }


}