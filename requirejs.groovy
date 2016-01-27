import org.gradle.api.Project
import org.gradle.api.file.ConfigruableFileTree

import org.gradle.api.Plugin
import org.gradle.api.artifacts.Configuration
import org.gradle.api.tasks.Copy

class RequireJsOptimizerExtension {
  static final String NAME = 'requirejsOptimizer'
  
  ConfigruableFileTree unpackedOptimizerLibraries
  
  static RequireJsOptimizerExtension get(Project project){
    return project.extensions.getByType(RequireJsOptimizerExtension)
  }
}


class RequireJsOptimizerPlugin implements Plugin<Project> {
  static final String LIBRARIES_CONFIGURATION_NAME = 'requirejsOptimizerLibraries'
  static final String UNPACK_LIBRARIES_TASK_NAME = 'unpackRequirejsOptimizerLibraries'
  
  private RequireJsOptimizerExtension extension
  
  void apply(Project project) {
    extension = project.extensions.create(RequireJsOptimizerExtension.NAME, RequireJsOptimizerExtension)
    
    
  }
}

class RequireJsOptimizerTask extends DefaultTask {
  File rDotJs
  
  def configurationFile
  def outputDir
  def logLevel = LogLevel.INFO
  Map<String, Object> extraOptions = [:]
  
  @TaskAction
  void optimizeWebAssets() {
    Map<String, Object> options = [
      dir: getOutputDir(),
      logLevel: getLogLevel().ordinal()
    ]
    
    runRequireJsOptimizer(options + extraOptions)
    
    relocateCssFiles() // The optimizer writes CSS files into the root of $outputDir, but we want them in $outputDir/css
  }
  
  @InputFile
  File getConfigurationFile(){
    return project.file(configurationFile)
  }
  
  @OutputDirectory
  File getOutputDir(){
    return project.file(outputDir)
  }
  
  LogLevel getLogLevel(){
    return (logLevel instanceof LogLevel) ? logLevel : LogLevel.valueOf((logLevel as String).toUpperCase())
  }
  
  private void runRequireJsOptimizer(Map<String, Object> options){
    List<String> optimizerArgs = [getConfigurationFile().absolutePath]
    optimizerArgs.addAll(options.collect {k, v -> "$k=${v as String}"})
    
    runInNodeJs(rDotJs, ['-o'] + optimizerArgs)
  }
  
  private void runInNodeJs(File script, List<String> args) {
    List<String> command = ['node', script.absolutePath] + args
    Process process = command.execute()
    
    process.waitForProcessOutput(System.out, System.err)
    
    if(process.exitValue() != 0) {
      throw new GradleException("Node (running r.js) exited with status ${process.exitValue()}.")
    }
  }
  
  private void relocateCssFiles(){
    FileTree optimizedCss = project.fileTree(getOutputDir()) { include ('*.css') }
    
    project.copy {
      from optimizedCss
      into new File(getOutputDir(), 'css')
    }
    
    project.delete optimizedCss
  }
  
  static enum LogLevel {
    TRACE, INFO, WARN, ERROR, SILENT
  }
}
