import java.util.concurrent.TimeUnit
import org.gradle.api.Project
import org.jetbrains.kotlin.gradle.plugin.extraProperties

fun getMockUrlBasedOnDeviceType(project: Project): String = runCatching {
  val rootProj = project.rootProject
  if (
    rootProj.extraProperties.properties.containsKey("isMockUrlConfigured") &&
    rootProj.extraProperties["isMockUrlConfigured"] == true
  ) return rootProj.extraProperties["mockUrl"] as String

  val adbDevicesOutput = "adb devices".execute().trim()
  if (adbDevicesOutput.lines().size > 2) {
    println("Found more than one device/emulator connected, please disconnect all but one to get automatic mock url setup")
  }
  val deviceName = adbDevicesOutput.lines()[1].split("\t")[0]
  return if (deviceName.startsWith("emulator")) {
    """"http://10.0.2.2:1080/""""
  } else {
    "adb reverse tcp:1080 tcp:1080".execute()
    """"http://localhost:1080/""""
  }.also {
    println("App configured for running on device: $deviceName, using mockUrl: $it")
    rootProj.extraProperties["mockUrl"] = it
    rootProj.extraProperties["isMockUrlConfigured"] = true
  }
}.onFailure {
  if (it is IndexOutOfBoundsException) {
    println("No device/emulator found, please connect one to get automatic mock url setup")
  } else {
    println("Error while getting mock url: ${it.message}")
    println("Using emulator setup")
  }
}.getOrElse { """"http://10.0.2.2:1080/"""" }


fun String.execute(): String {
  val parts = this.split("\\s".toRegex())
  val proc = ProcessBuilder(*parts.toTypedArray())
    .redirectOutput(ProcessBuilder.Redirect.PIPE)
    .redirectError(ProcessBuilder.Redirect.PIPE).start()
  proc.waitFor(30, TimeUnit.SECONDS)
  return proc.inputStream.bufferedReader().readText()
}
