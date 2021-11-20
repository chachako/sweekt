import com.github.gmazzo.gradle.plugins.BuildConfigExtension

plugins { id(Plugins.BuildConfig) apply false }

subprojects {
  apply(plugin = Plugins.BuildConfig)
  afterEvaluate {
    configure<BuildConfigExtension> {
      with(publication.data) {
        buildConfigField("String", "Version", "\"$version\"")
        buildConfigField("String", "GroupId", "\"$groupId\"")
        buildConfigField("String", "CompilerId", "\"${findPropertyOrEnv("compiler.id")}\"")
        // The compiler plugin id has different suffix.
        buildConfigField("String", "CompilerArtifactId", "\"${artifactId.replace("-gradle", "-compiler")}\"")
        useKotlinOutput { internalVisibility = true }
      }
    }
  }
}