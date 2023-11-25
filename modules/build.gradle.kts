import dev.iurysouza.modulegraph.Theme

plugins {
  id("dev.iurysouza.livematch.linter")
  alias(libs.plugins.moduleGraph)
}

moduleGraphConfig {
  readmePath.set("${projectDir.parent}/README.md")
  heading.set("### Module Graph")
  theme.set(
    Theme.BASE(
      mapOf(
        "primaryTextColor" to "#fff",
        "primaryColor" to "#5a4f7c",
        "primaryBorderColor" to "#5a4f7c",
        "lineColor" to "#f5a623",
        "tertiaryColor" to "#40375c",
        "fontSize" to "11px",
      ),
    ),
  )
}
