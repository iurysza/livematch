val ktlint by configurations.creating

dependencies {
  ktlint("com.pinterest:ktlint:0.47.1") {
    attributes {
      attribute(Bundling.BUNDLING_ATTRIBUTE, objects.named(Bundling.EXTERNAL))
    }
  }
}

val outputDir = "${project.buildDir}/reports/ktlint/"
val inputFiles = project.fileTree(mapOf("dir" to rootDir, "include" to "**/*.kt"))
val reportDir = "${rootProject.buildDir}/reports/ktlint"
val ktlintGlobPattern = arrayOf(
  "**/*.kt",
  "!**/build/**",
  "!**/test/**/",
  "!**/androidTest/**/",
)
val ktlintArgs = listOf(
  *ktlintGlobPattern,
  "--relative",
  "--reporter=checkstyle,output=$reportDir/ktlint-checkstyle.xml",
  "--reporter=html,output=$reportDir/report.html",
)

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

// |--------------------------------------------------
// | Detekt Setup
// |--------------------------------------------------

val detekt by configurations.creating
val detektTask = tasks.register<JavaExec>("detekt") {
  mainClass.set("io.gitlab.arturbosch.detekt.cli.Main")
  classpath = detekt
  group = "verification"

  val reportDir = "${rootProject.buildDir}/reports/detekt"
  val params = listOf(
    "--input", rootDir.absolutePath,
    "--excludes", "**/build/** , **/test/** , **/androidTest/**",
    "--config", "${rootDir.parent}/build-logic/linter/detekt-config.yml",
    "--report", "xml:$reportDir/checkstyle.xml",
    "--report", "html:$reportDir/report.html",
  )

  args(params)
}

dependencies {
  detekt("io.gitlab.arturbosch.detekt:detekt-cli:1.22.0")
}
