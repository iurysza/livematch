import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.DependencyHandlerScope
import org.gradle.kotlin.dsl.getByType
import org.gradle.kotlin.dsl.project

private val Project.versionCatalogLibs
  get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.findLibraryAlias(library: Versions.Lib): Provider<MinimalExternalModuleDependency> {
  return runCatching {
    versionCatalogLibs.findLibrary(library.alias).get()
  }.onFailure {
    println("Library $library not found in version catalog")
  }.getOrNull()!!
}

fun DependencyHandlerScope.project(module: ProjectModule) = project(module.path)

sealed class ProjectModule(val path: String) {
  object MatchThread : ProjectModule(":features:match-thread")
  object MatchDay : ProjectModule(":features:match-day")
  object DesignSystem : ProjectModule(":core:design-system")
  object Common : ProjectModule(":core:common")
  object FootballInfo : ProjectModule(":core:footballinfo")
  object FootballData : ProjectModule(":core:footballdata")
  object Reddit : ProjectModule(":core:reddit")
}
