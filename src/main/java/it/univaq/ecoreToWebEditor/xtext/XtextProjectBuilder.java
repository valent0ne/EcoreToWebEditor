package it.univaq.ecoreToWebEditor.xtext;

import com.google.common.base.Charsets;
import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;
import org.eclipse.xtext.util.Files;
import org.eclipse.xtext.util.XtextVersion;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;
import org.eclipse.xtext.xtext.wizard.BuildSystem;
import org.eclipse.xtext.xtext.wizard.IdeProjectDescriptor;
import org.eclipse.xtext.xtext.wizard.LanguageDescriptor;
import org.eclipse.xtext.xtext.wizard.ProjectLayout;
import org.eclipse.xtext.xtext.wizard.RuntimeProjectDescriptor;
import org.eclipse.xtext.xtext.wizard.SourceLayout;
import org.eclipse.xtext.xtext.wizard.TestProjectDescriptor;
import org.eclipse.xtext.xtext.wizard.UiProjectDescriptor;
import org.eclipse.xtext.xtext.wizard.WebProjectDescriptor;
import org.eclipse.xtext.xtext.wizard.WizardConfiguration;
import org.eclipse.xtext.xtext.wizard.cli.CliProjectsCreator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class XtextProjectBuilder {

  //logger
  private static final Logger LOGGER = LoggerFactory.getLogger(XtextProjectBuilder.class);

  private String BASE_NAME;
  
  private String FORMAT_NAME;
  
  private String PATH_TO_PROJECT_FOLDER;
  
  private final String XTEXT_VERSION = "2.12.0";

  
  public XtextProjectBuilder(final String baseName, final String formatName, final String pathToProjectFolder) {
    this.BASE_NAME = baseName;
    this.FORMAT_NAME = formatName;
    this.PATH_TO_PROJECT_FOLDER = pathToProjectFolder;

    LOGGER.info("[XTEXTPROJECTBUILDER - DATA RECAP]");
    LOGGER.info("BASE_NAME: {}", BASE_NAME);
    LOGGER.info("FORMAT_NAME: {}", FORMAT_NAME);
    LOGGER.info("PATH_TO_PROJECT_FOLDER: {}", PATH_TO_PROJECT_FOLDER);

  }
  
  public void run() {

    LOGGER.info("[XTEXTPROJECTBUILDER - START]");

    final CliProjectsCreator creator = this.newProjectCreator();
    final Consumer<WizardConfiguration> _function = (WizardConfiguration config) -> {
      try {
        String _baseName = config.getBaseName();
        final File targetLocation = new File(this.PATH_TO_PROJECT_FOLDER, _baseName);
        targetLocation.mkdirs();
        Files.sweepFolder(targetLocation);
        String _path = targetLocation.getPath();
        config.setRootLocation(_path);
        creator.createProjects(config);
      } catch (Throwable _e) {
        throw Exceptions.sneakyThrow(_e);
      }
    };
    try{
      this.projectConfigs.forEach(_function);
    }catch (Throwable e){
      LOGGER.error(e.getMessage());
      LOGGER.error("[XTEXTPROJECTBUILDER - ABORTED]");
      System.exit(1);
    }


    LOGGER.info("[XTEXTPROJECTBUILDER - DONE]");

  }
  
  private final List<WizardConfiguration> projectConfigs = Collections.<WizardConfiguration>unmodifiableList(CollectionLiterals.<WizardConfiguration>newArrayList(ObjectExtensions.<WizardConfiguration>operator_doubleArrow(
    this.newProjectConfig(), ((Procedure1<WizardConfiguration>) (WizardConfiguration it) -> {
    it.setBaseName(this.BASE_NAME);
    it.setPreferredBuildSystem(BuildSystem.MAVEN);
    it.setSourceLayout(SourceLayout.PLAIN);
    it.setProjectLayout(ProjectLayout.HIERARCHICAL);
    RuntimeProjectDescriptor _runtimeProject = it.getRuntimeProject();
    TestProjectDescriptor _testProject = _runtimeProject.getTestProject();
    _testProject.setEnabled(false);
    UiProjectDescriptor _uiProject = it.getUiProject();
    _uiProject.setEnabled(true);
    UiProjectDescriptor _uiProject_1 = it.getUiProject();
    TestProjectDescriptor _testProject_1 = _uiProject_1.getTestProject();
    _testProject_1.setEnabled(false);
    IdeProjectDescriptor _ideProject = it.getIdeProject();
    _ideProject.setEnabled(true);
    WebProjectDescriptor _webProject = it.getWebProject();
    _webProject.setEnabled(true);
    it.setLineDelimiter("\n");
  }))));
  
  private WizardConfiguration newProjectConfig() {
    WizardConfiguration _wizardConfiguration = new WizardConfiguration();
    final Procedure1<WizardConfiguration> _function = (WizardConfiguration it) -> {
      XtextVersion _xtextVersion = new XtextVersion(this.XTEXT_VERSION);
      it.setXtextVersion(_xtextVersion);
      it.setEncoding(Charsets.UTF_8);
      LanguageDescriptor _language = it.getLanguage();
      final Procedure1<LanguageDescriptor> _function_1 = (LanguageDescriptor it_1) -> {
        it_1.setName("org.xtext.example.testdsl.TestDsl");
        LanguageDescriptor.FileExtensions _fromString = LanguageDescriptor.FileExtensions.fromString("testdsl");
        it_1.setFileExtensions(_fromString);
      };
      ObjectExtensions.<LanguageDescriptor>operator_doubleArrow(_language, _function_1);
    };
    return ObjectExtensions.<WizardConfiguration>operator_doubleArrow(_wizardConfiguration, _function);
  }
  
  private CliProjectsCreator newProjectCreator() {
    CliProjectsCreator _cliProjectsCreator = new CliProjectsCreator();
    final Procedure1<CliProjectsCreator> _function = (CliProjectsCreator it) -> {
      it.setLineDelimiter("\n");
    };
    return ObjectExtensions.<CliProjectsCreator>operator_doubleArrow(_cliProjectsCreator, _function);
  }
}
