pluginManagement {
    repositories {
        gradlePluginPortal()
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
}

rootProject.name = "library-services"

include(":book-service")
include(":user-service")
include(":api-gateway")
include(":auth-service")
include(":admin-cli")
include(":shared")

project(":book-service").projectDir = file("book-service")
project(":user-service").projectDir = file("user-service")
project(":api-gateway").projectDir = file("api-gateway")
project(":auth-service").projectDir = file("auth-service")
project(":admin-cli").projectDir = file("admin-cli") 