pluginManagement {
    repositories {
        maven("https://repo.papermc.io/repository/maven-public/")
        gradlePluginPortal()
    }
}
rootProject.name = "eco"
include(":eco-api", ":eco-core", ":eco-core:core-backend", ":eco-core:core-plugin")
include(":eco-core:core-nms", ":eco-core:core-nms:v26_2")
