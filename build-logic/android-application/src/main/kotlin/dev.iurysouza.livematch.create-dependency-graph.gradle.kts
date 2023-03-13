task("createMermaidDependencyGraph") {
  group = "Reporting"
  description = "Creates a mermaid dependency graph for the project"
  doLast {
    val projects = mutableSetOf<Project>()
    val dependencies = mutableListOf<Pair<Project, Project>>()
    val queue = mutableListOf(rootProject)
    while (queue.isNotEmpty()) {
      val project = queue.removeAt(0)
      queue.addAll(project.childProjects.values)

      project.configurations.forEach { config ->
        config.dependencies
          .withType(ProjectDependency::class.java)
          .map { it.dependencyProject }
          .forEach { dependency ->
            if (dependency != project) {
              projects.add(project)
              projects.add(dependency)
              dependencies.add(project to dependency)
            }
          }
      }
    }
    val sortedProjects = projects.toMutableList().sortedBy { it.path }

    val mermaid = buildMermaidGraph(sortedProjects, dependencies)
    println(mermaid)
    appendMermaidGraphToReadme(mermaid)
  }
}

fun buildMermaidGraph(
  sortedProjects: List<Project>,
  dependencies: MutableList<Pair<Project, Project>>,
): String {
  var mermaid = """
      %%{
        init: {
          'theme': 'dark',
          'themeVariables': {
            'primaryColor': '#C4C7B300',
            'primaryTextColor': '#fff',
            'primaryBorderColor': '#7C0000',
            'lineColor': '#FF2F2F2F',
            'secondaryColor': '#006100',
            'tertiaryColor': '#fff'
          }
        }
      }%%
    """.trimIndent()
  mermaid += "\n\ngraph LR\n"

  // Split the path of each project into parts and keep only the non-blank parts
  val modules = sortedProjects.map { it.path.split(":").filter { it.isNotBlank() } }
  // Get the distinct group names
  val groups = modules.map { it[0] }.distinct()
  // For each group
  groups.forEach { group ->
    // Add a subgraph header with the group name
    mermaid += "  subgraph $group\n"
    // For each module in the group
    modules.filter { it[0] == group }.forEach {
      // Add the module name to the subgraph
      mermaid += "    ${it[1]}\n"
    }
    mermaid += "  end\n"
  }

  dependencies.forEach { (key, value) ->
    val sourceName = key.path.split(":").filter { it.isNotBlank() }[1]
    val targetName = value.path.split(":").filter { it.isNotBlank() }[1]
    mermaid += "  $sourceName --> $targetName\n"
  }
  return mermaid
}

fun appendMermaidGraphToReadme(mermaid: String) {
  val readmeFile = File(rootProject.rootDir.parent, "README.md")
  val readmeText = readmeFile.readText()

  val architectureSectionStart = readmeText.indexOf("# Dependency Diagram")
  if (architectureSectionStart == -1) {
    throw Exception("`# Dependency graph` section not found in the README.md file")
  }

  println(architectureSectionStart)
  val architectureSectionEnd = readmeText.indexOf("#", architectureSectionStart + 1)
  val architectureSection = if (architectureSectionEnd == -1) {
    readmeText.substring(architectureSectionStart)
  } else {
    readmeText.substring(architectureSectionStart, architectureSectionEnd)
  }

  val updatedReadmeText = readmeText.replace(
    architectureSection,
    "$architectureSection\n\n```mermaid\n${mermaid}\n```\n\n",
  )

  readmeFile.writeText(updatedReadmeText)
}
