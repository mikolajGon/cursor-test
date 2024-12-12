buildscript {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.22")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.9.22")
    }
}

plugins {
    kotlin("jvm") version "1.9.22" apply false
    kotlin("plugin.serialization") version "1.9.22" apply false
    id("io.ktor.plugin") version "2.3.7" apply false
}

allprojects {
    repositories {
        mavenCentral() {
            metadataSources {
                mavenPom()
                artifact()
            }
        }
        maven {
            url = uri("https://jitpack.io")
            metadataSources {
                mavenPom()
                artifact()
            }
        }
    }

    // Set Java compatibility for all projects
    afterEvaluate {
        if (plugins.hasPlugin("org.jetbrains.kotlin.jvm")) {
            tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
                kotlinOptions {
                    jvmTarget = "17"
                    apiVersion = "1.9"
                    languageVersion = "1.9"
                }
            }
            tasks.withType<JavaCompile> {
                sourceCompatibility = JavaVersion.VERSION_17.toString()
                targetCompatibility = JavaVersion.VERSION_17.toString()
            }
        }
    }
} 