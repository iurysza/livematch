@file:Suppress("SpellCheckingInspection")

import io.gitlab.arturbosch.detekt.Detekt
import org.jlleitschuh.gradle.ktlint.reporter.ReporterType

plugins {
  id("io.gitlab.arturbosch.detekt") apply false
  id("org.jlleitschuh.gradle.ktlint") apply false
}

allprojects {
  apply {
    plugin("org.jlleitschuh.gradle.ktlint")
    plugin("io.gitlab.arturbosch.detekt")
  }
  detekt {
    config = rootProject.files("config/detekt/detekt.yml")
  }
  dependencies {
    detektPlugins("io.nlopez.compose.rules:detekt:${Versions.detektComposePlugin}")
  }
  ktlint {
    verbose.set(true)
    android.set(true)
    outputToConsole.set(true)

    debug.set(false)
    ignoreFailures.set(false)
    enableExperimentalRules.set(false)
    reporters {
      reporter(ReporterType.PLAIN_GROUP_BY_FILE)
      reporter(ReporterType.HTML)
    }

    filter {
      exclude("**/generated/**")
      include("**/java/**")
      include("**/kotlin/**")
    }
  }

}

val file = file("${rootProject.rootDir}/build/reports/detekt.html")

tasks.withType<Detekt>().configureEach {
  reports {
    html.required.set(true)
    html.outputLocation.set(file)
  }
}

task("ktlintFormatAll") {
  group = "verification"
  description = "Runs ktlint format on all modules"
  dependsOn(subprojects.map { it.tasks.named("ktlintFormat") })
}

task("ktlintAll") {
  group = "verification"
  description = "Runs ktlint on all modules"
  dependsOn(subprojects.map { it.tasks.named("ktlintCheck") })
}

task("detektAll") {
  group = "verification"
  description = "Runs detekt on all modules"
  dependsOn(subprojects.map { it.tasks.named("detekt") })
}

task("preMergeCheck") {
  group = "verification"
  description = "Runs ktlint and detekt on all modules"
  dependsOn("ktlintAll", "detektAll")
}
