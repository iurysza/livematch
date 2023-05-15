import io.gitlab.arturbosch.detekt.Detekt

plugins {
  id("io.gitlab.arturbosch.detekt") apply false
}

allprojects {
  apply {
    plugin("io.gitlab.arturbosch.detekt")
  }
  detekt {
    config = rootProject.files("config/detekt/detekt.yml")
  }
}

val file = file("${rootProject.rootDir}/build/reports/detekt.html")

tasks.withType<Detekt>().configureEach {
  reports {
    html.required.set(true)
    html.outputLocation.set(file)
  }
}

task("detektAll") {
  group = "verification"
  description = "Runs detekt on all modules"
  dependsOn(subprojects.map { it.tasks.named("detekt") })
}
