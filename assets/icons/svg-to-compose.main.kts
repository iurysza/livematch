@file:Repository("https://jitpack.io")
@file:Repository("https://maven.google.com")
@file:Repository("https://repo1.maven.org/maven2/")
@file:Repository("https://jetbrains.bintray.com/trove4j")

@file:DependsOn("com.github.DevSrSouza:svg-to-compose:-SNAPSHOT")
@file:DependsOn("com.google.guava:guava:23.0")
@file:DependsOn("com.android.tools:sdk-common:27.2.0-alpha16")
@file:DependsOn("com.android.tools:common:27.2.0-alpha16")
@file:DependsOn("com.squareup:kotlinpoet:1.7.2")
@file:DependsOn("org.ogce:xpp3:1.1.6")

import br.com.devsrsouza.svg2compose.Svg2Compose
import br.com.devsrsouza.svg2compose.VectorType
import java.io.File


Svg2Compose.parse(
    applicationIconPackage = "dev.iurysouza.livematch.designsystem.theme",
    accessorName = "LiveMatchAssets",
    outputSourceDirectory = File("../../modules/core/design-system/src/main/java/"),
    vectorsDirectory = File("./svgs/"),
    type = VectorType.SVG,
    allAssetsPropertyName = "AllAssets"
)
