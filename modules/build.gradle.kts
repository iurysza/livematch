plugins {
  id("dev.iurysouza.livematch.linter")
  alias(libs.plugins.moduleGraph)
}

moduleGraphConfig {
  readmePath.set("${projectDir.parent}/README.md")
  heading.set("### Dependency Diagram")
}
