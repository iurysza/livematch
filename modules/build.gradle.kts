@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once we bump to Gradle 8.1
plugins {
  id("dev.iurysouza.livematch.linter")
  alias(libs.plugins.moduleGraph)
}

moduleGraphConfig {
  readmePath.set("${projectDir.parent}/README.md")
  heading.set("### Dependency Diagram")
}
