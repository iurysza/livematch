import org.gradle.api.Project
import org.gradle.api.artifacts.MinimalExternalModuleDependency
import org.gradle.api.artifacts.VersionCatalog
import org.gradle.api.artifacts.VersionCatalogsExtension
import org.gradle.api.provider.Provider
import org.gradle.kotlin.dsl.getByType

private val Project.versionCatalogLibs
  get(): VersionCatalog = extensions.getByType<VersionCatalogsExtension>().named("libs")

fun Project.findLibraryAlias(library: Versions.Lib): Provider<MinimalExternalModuleDependency> {
  return runCatching {
    versionCatalogLibs.findLibrary(library.alias).get()
  }.onFailure {
    println("Library $library not found in version catalog")
  }.getOrNull()!!
}


