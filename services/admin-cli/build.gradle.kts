val ktor_version: String by project
val kotlin_version: String by project
val kmongo_version: String = "4.8.0"

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
    application
}

application {
    mainClass.set("com.library.admin.MainKt")
}

dependencies {
    implementation("com.github.ajalt.clikt:clikt:4.2.1")
    implementation("org.litote.kmongo:kmongo:$kmongo_version")
    implementation("org.litote.kmongo:kmongo-coroutine:$kmongo_version")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation("at.favre.lib:bcrypt:0.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
} 