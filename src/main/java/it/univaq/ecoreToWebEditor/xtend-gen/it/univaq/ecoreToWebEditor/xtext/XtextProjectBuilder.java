package it.univaq.ecoreToWebEditor.xtext;

import com.google.common.base.Charsets;
import it.univaq.ecoreToWebEditor.core.Main;
import it.univaq.ecoreToWebEditor.utils.Constants;
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

@SuppressWarnings("all")
public class XtextProjectBuilder {
  private final String XTEXT_VERSION = "2.12.0";
  
  private final static Logger LOGGER = LoggerFactory.getLogger(XtextProjectBuilder.class.getClass());
  
  public XtextProjectBuilder() {
  }
  
  public void run() {
    XtextProjectBuilder.LOGGER.info(((Constants.ANSI_GREEN + "[XTEXTPROJECTBUILDER - START]") + Constants.ANSI_RESET));
    try {
      final CliProjectsCreator creator = this.newProjectCreator();
      final Consumer<WizardConfiguration> _function = (WizardConfiguration config) -> {
        try {
          String _baseName = config.getBaseName();
          final File targetLocation = new File(Main.PATH_TO_OUT_FOLDER, _baseName);
          targetLocation.mkdirs();
          Files.sweepFolder(targetLocation);
          String _path = targetLocation.getPath();
          config.setRootLocation(_path);
          creator.createProjects(config);
        } catch (Throwable _e) {
          throw Exceptions.sneakyThrow(_e);
        }
      };
      this.projectConfigs.forEach(_function);
    } catch (final Throwable _t) {
      if (_t instanceof Throwable) {
        final Throwable e = (Throwable)_t;
        String _message = e.getMessage();
        XtextProjectBuilder.LOGGER.error(_message);
        e.printStackTrace();
        XtextProjectBuilder.LOGGER.error(((Constants.ANSI_RED + "[XTEXTPROJECTBUILDER - ABORTED]") + Constants.ANSI_RESET));
        System.exit(1);
      } else {
        throw Exceptions.sneakyThrow(_t);
      }
    }
    XtextProjectBuilder.LOGGER.info(((Constants.ANSI_GREEN + "[XTEXTPROJECTBUILDER - DONE]") + Constants.ANSI_RESET));
  }
  
  private final List<WizardConfiguration> projectConfigs = Collections.<WizardConfiguration>unmodifiableList(CollectionLiterals.<WizardConfiguration>newArrayList(ObjectExtensions.<WizardConfiguration>operator_doubleArrow(
    this.newProjectConfig(), ((Procedure1<WizardConfiguration>) (WizardConfiguration it) -> {
    it.setBaseName(Main.BASE_NAME);
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
        it_1.setName(Main.LANGUAGE_NAME);
        LanguageDescriptor.FileExtensions _fromString = LanguageDescriptor.FileExtensions.fromString(Main.FORMAT_NAME);
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
