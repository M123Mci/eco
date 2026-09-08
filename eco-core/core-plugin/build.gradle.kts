group = "com.willfp"
version = rootProject.version

val paperVersion: String by project
val externalPluginLibDir = rootProject.extra["externalPluginLibDir"].toString()

dependencies {
    compileOnly(project(":eco-core:core-backend"))

    implementation("org.bstats:bstats-bukkit:3.2.1")

    // Libraries (provided at runtime via Paper library loader)
    compileOnly("com.mysql:mysql-connector-j:9.6.0")
    compileOnly("org.mariadb.jdbc:mariadb-java-client:2.7.12")
    implementation("org.jetbrains.exposed:exposed-core:1.2.0")
    implementation("org.jetbrains.exposed:exposed-jdbc:1.2.0")
    compileOnly("com.zaxxer:HikariCP:7.0.2")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
    implementation("org.mongodb:mongodb-driver-kotlin-coroutine:5.6.2")
    compileOnly("io.hotmoka:toml4j:0.7.3") {
        exclude(group = "com.google.code.gson", module = "gson")
    }
    implementation("com.willfp:ModelEngineBridge:1.4.0")

    // Included in spigot jar
    compileOnly("io.papermc.paper:paper-api:$paperVersion")

    // Plugin dependencies
    compileOnly("com.sk89q.worldguard:worldguard-bukkit:7.0.15") {
        exclude("*", "*")
    }
    compileOnly("com.palmergames.bukkit.towny:towny:0.102.0.9") {
        exclude(group = "com.zaxxer", module = "HikariCP")
    }
    compileOnly("com.gmail.nossr50.mcMMO:mcMMO:2.2.049")
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("com.github.LoneDev6:API-ItemsAdder:2.4.7")
    compileOnly("com.nexomc:nexo:1.19.1") {
        exclude(group = "*", module = "*")
    }
    compileOnly(files("$externalPluginLibDir/craft-engine-core-26.8.2.jar"))
    compileOnly(files("$externalPluginLibDir/craft-engine-bukkit-26.8.2.jar"))
    compileOnly("com.arcaniax:HeadDatabase-API:1.3.2")
    compileOnly("net.essentialsx:EssentialsX:2.21.2") {
        exclude(group = "*", module = "*")
    }
    compileOnly("com.bgsoftware:SuperiorSkyblockAPI:2025.2.1")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7") {
        exclude(group = "*", module = "*")
    }
    compileOnly("com.github.N0RSKA:ScytherAPI:55a")
    compileOnly("org.black_ixx:playerpoints:3.3.3")
    compileOnly(files("$externalPluginLibDir/MythicMobsPremium-5.13.1-SNAPSHOT.jar"))
    compileOnly(files("$externalPluginLibDir/IridiumSkyblock-4.1.2.jar"))
    compileOnly("net.william278.huskclaims:huskclaims-bukkit:1.5.10")
    compileOnly("net.william278.husktowns:husktowns-bukkit:3.1.4")
    compileOnly("com.github.jojodmo:ItemBridge:b0054538c1")
    compileOnly("su.nightexpress.excellenteconomy:ExcellentEconomy:2.8.0")
    compileOnly("su.nightexpress.nightcore:main:2.16.4")
    compileOnly("su.nightexpress.excellentshop:Core:5.1.3")
    compileOnly("dev.kitteh:factions:4.4.0")
    compileOnly("com.github.Zrips:Residence:6.0.2.3") {
        exclude(group = "*", module = "*")
    }

    compileOnly(fileTree(externalPluginLibDir) {
        include("BentoBox-1.20.0.jar", "CMI-API9.8.6.4.jar", "CMILib1.5.9.9.jar",
            "DeluxeCombat API.jar", "DeluxeMenus-1.13.7-DEV-156.jar",
            "DeluxeSellwands Build 22e.jar", "FabledSkyBlock-4.2.2.jar",
            "GriefPrevention.jar", "denizen-1.3.0-SNAPSHOT.jar")
    })
}

tasks {
    shadowJar {
        relocate("org.bstats", "com.willfp.eco.libs.bstats")
        minimize {
            exclude(dependency("org.mongodb:.*:.*"))
            exclude(dependency("org.jetbrains.exposed:.*:.*"))
            exclude(dependency("com.willfp:ModelEngineBridge:.*"))
        }
    }

    processResources {
        filesMatching(listOf("**plugin.yml", "**eco.yml")) {
            expand("projectVersion" to project.version)
        }
    }
}
