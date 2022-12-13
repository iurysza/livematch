val ktlint: Configuration by configurations.creating

dependencies {
  ktlint("com.pinterest:ktlint:0.47.1") {
    attributes {
      attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    }
  }
}

val inputFiles: ConfigurableFileTree = project.fileTree(mapOf("dir" to rootDir, "include" to "**/*.kt"))
val reportDir = "${rootProject.buildDir}/reports/"
val outputDir = reportDir

val ktlintArgs = listOf(
  "**/*.kt",
  "!**/build/**",
  "!**/test/**/",
  "!**/androidTest/**/",
  "--relative",
  "--reporter=checkstyle,output=$reportDir/ktlint-checkstyle.xml",
  "--reporter=html,output=$reportDir/ktlint-report.html",
)

// |--------------------------------------------------
// | Linting Tasks
// |--------------------------------------------------

val ktlintCheck by tasks.creating(JavaExec::class) {
  inputs.files(inputFiles)
  outputs.dir(outputDir)
  classpath = ktlint
  mainClass.set("com.pinterest.ktlint.Main")

  group = "verification"
  description = "Check Kotlin code style."
  args(ktlintArgs)
}

val ktlintFormat by tasks.creating(JavaExec::class) {
  inputs.files(inputFiles)
  outputs.dir(outputDir)

  group = "verification"
  description = "Fix code style issues"
  classpath = ktlint
  mainClass.set("com.pinterest.ktlint.Main")

  args(ktlintArgs + "--format")
}

val detekt: Configuration by configurations.creating
dependencies {
  detekt("io.gitlab.arturbosch.detekt:detekt-cli:1.22.0")
}

val detektCheck = tasks.register<JavaExec>("detekt") {
  mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
  classpath = detekt
  group = "verification"

  args(
    listOf(
      "--input", rootDir.absolutePath,
      "--excludes", "**/build/**, **/test/**, **/androidTest/**",
      "--config", "${rootDir.parent}/build-logic/linter/detekt-config.yml",
      "--report", "xml:$reportDir/detekt-checkstyle.xml",
      "--report", "html:$reportDir/detekt-report.html",
    ),
  )
}
