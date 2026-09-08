dependencies {
    compileOnly("io.papermc.paper:paper-api:${rootProject.property("paperVersion")}")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
}
java { withJavadocJar() }
publishing {
    publications {
        create<MavenPublication>("api") {
            from(components["java"])
            artifactId = "eco"
        }
    }
    repositories {
        maven {
            name = "localPlugins"
            url = uri(providers.gradleProperty("localPluginRepoDir").orElse("D:/Minecraft/PluginLibs/Maven").get())
        }
    }
}
