plugins {
  id("dev.iurysouza.livematch.linter")
  id("dev.iurysouza.modulegraph") version ("0.2.2")
}

moduleGraphConfig {
  readmePath.set("${projectDir.parent}/README.md")
  heading.set("### Dependency Diagram")
}
