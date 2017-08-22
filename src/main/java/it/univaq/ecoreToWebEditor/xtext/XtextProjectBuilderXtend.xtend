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

public class XtextProjectBuilderXtend{

    private String BASE_NAME;               //ex. "org.xtext.example.testDsl"
    private String FORMAT_NAME;             //ex. "testdsl"
    private String PATH_TO_PROJECT_FOLDER;

    private final String XTEXT_VERSION = "2.12.0"

    new(String baseName, String formatName, String pathToProjectFolder){
        BASE_NAME = baseName;
        FORMAT_NAME = formatName;
        PATH_TO_PROJECT_FOLDER = pathToProjectFolder;
    }

    def void run() {
        val creator = newProjectCreator
        projectConfigs.forEach [ config |
            val targetLocation = new File(PATH_TO_PROJECT_FOLDER, config.baseName)
            targetLocation.mkdirs
            org.eclipse.xtext.util.Files.sweepFolder(targetLocation)
            config.rootLocation = targetLocation.path
            creator.createProjects(config)
            println("Updating expectations for " + config.baseName)
        ]
    }

    val projectConfigs = #[
        newProjectConfig => [
            baseName = BASE_NAME
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
                name = "org.xtext.example.testdsl.TestDsl"
                fileExtensions = FileExtensions.fromString("testdsl")
            ]
        ]
    }

    private def newProjectCreator() {
        new CliProjectsCreator => [
            lineDelimiter = "\n"
        ]
    }


}