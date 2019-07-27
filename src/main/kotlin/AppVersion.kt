package bsync

import kotlinx.serialization.*
import kotlinx.serialization.json.Json

@Serializable
data class VersionInfo(
    val majorVersion: String = "00",
    val minorVersion: String = "00",
    val name: String  = "Kotlin-Prototype")

val vInfo = VersionInfo()


@UseExperimental(kotlinx.serialization.ImplicitReflectionSerializer::class)
fun versionText(): String {

    return Json.stringify(vInfo)
}
