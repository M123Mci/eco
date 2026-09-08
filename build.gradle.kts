import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("java-library")
    id("com.gradleup.shadow") version "9.4.1"
    id("maven-publish")
    kotlin("jvm") version "2.3.21"
}

group = "com.willfp"
version = providers.gradleProperty("version").get()

val localPluginRepoDir = providers.gradleProperty("localPluginRepoDir")
    .orElse("D:/Minecraft/PluginLibs/Maven").get()
val externalPluginLibDir = providers.gradleProperty("externalPluginLibDir")
    .orElse("D:/Minecraft/PluginLibs/Jars").get()
extra["externalPluginLibDir"] = externalPluginLibDir

dependencies {
    implementation(project(":eco-api"))
    implementation(project(path = ":eco-core:core-plugin", configuration = "shadow"))
    implementation(project(":eco-core:core-backend"))
    implementation(project(path = ":eco-core:core-nms:v26_2", configuration = "shadow"))
}

allprojects {
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")
    apply(plugin = "com.gradleup.shadow")
    apply(plugin = "org.jetbrains.kotlin.jvm")
    group = rootProject.group
    version = rootProject.version

    repositories {
        mavenCentral()

        maven("https://repo.auxilor.io/repository/maven-public/")

        maven("https://jitpack.io") {
            content {
                includeGroupByRegex("com\\.github\\..*")
                excludeGroup("com.github.TownyAdvanced")
            }
        }

        // Paper
        maven("https://repo.papermc.io/repository/maven-public/")

        // EssentialsX
        maven("https://repo.essentialsx.net/releases")

        // SuperiorSkyblock2
        maven("https://repo.bg-software.com/repository/api/")

        // mcMMO, BentoBox
        maven("https://repo.codemc.io/repository/maven-public/")

        // Spigot API, Bungee API
        maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")

        // PlaceholderAPI
        maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")

        // ProtocolLib
        maven("https://repo.dmulloy2.net/nexus/repository/public/")

        // WorldGuard
        maven("https://maven.enginehub.org/repo/")

        // FactionsUUID
        //maven("https://ci.ender.zone/plugin/repository/everything/")

        // MythicMobs
        maven("https://mvn.lumine.io/repository/maven-public/")

        // LibsDisguises
        maven("https://mvn.lib.co.nz/public")

        // PlayerPoints
        maven("https://repo.rosewooddev.io/repository/public/")

        // IridiumSkyblock
        maven("https://nexus.iridiumdevelopment.net/repository/maven-releases/")

        // HuskPlugins
        maven("https://repo.william278.net/releases")

        // FancyHolograms
        maven("https://repo.fancyinnovations.com/releases")

        // Nexo
        maven("https://repo.nexomc.com/releases")

        // CraftEngine
        maven("https://repo.momirealms.net/releases/")

        // ExcellentEconomy and ExcellentShop
        maven("https://repo.nightexpressdev.com/releases")

        //Towny
        maven("https://repo.glaremasters.me/repository/towny/")

        // FactionsUUID
        exclusiveContent {
            forRepository {
                maven("https://dependency.download/releases")
            }

            filter {
                includeGroup("dev.kitteh")
            }
        }
    }

    repositories { maven { url = uri(localPluginRepoDir) } }
    dependencies {
        implementation(kotlin("stdlib", "2.3.21"))
        implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.2")
        compileOnly("org.jetbrains:annotations:26.1.0")
        compileOnly("net.kyori:adventure-api:5.2.0")
        compileOnly("net.kyori:adventure-text-serializer-gson:5.2.0")
        compileOnly("net.kyori:adventure-text-serializer-legacy:5.2.0")
        implementation("com.github.ben-manes.caffeine:caffeine:3.2.3")
    }
    java {
        toolchain.languageVersion.set(JavaLanguageVersion.of(25))
        withSourcesJar()
    }
    tasks.withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        options.release.set(25)
    }
    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        compilerOptions.jvmTarget.set(JvmTarget.JVM_25)
    }
    tasks.withType<AbstractArchiveTask>().configureEach {
        isPreserveFileTimestamps = false
        isReproducibleFileOrder = true
    }
    tasks.named("build") { dependsOn("shadowJar") }
}

tasks.shadowJar {
    archiveFileName.set("eco-${project.version}.jar")
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    exclude("META-INF/*.SF", "META-INF/*.RSA", "META-INF/*.DSA")
    relocate("org.apache.commons.lang3", "com.willfp.eco.libs.lang3")
    relocate("org.intellij", "com.willfp.eco.libs.intellij")
    relocate("org.jetbrains.annotations", "com.willfp.eco.libs.jetbrains.annotations")
    relocate("com.willfp.modelenginebridge", "com.willfp.eco.libs.modelenginebridge")
    relocate("com.github.benmanes.caffeine", "com.willfp.eco.libs.caffeine")
    // MongoDB 的协程签名必须与插件内 Kotlin 一起重定位，不能由外部加载器混用。
    relocate("com.mongodb", "com.willfp.eco.libs.mongodb")
    relocate("org.bson", "com.willfp.eco.libs.bson")
    relocate("org.reactivestreams", "com.willfp.eco.libs.reactivestreams")
    relocate("reactor", "com.willfp.eco.libs.reactor")
    relocate("kotlin", "com.willfp.eco.libs.kotlin") { exclude("kotlin.kotlin_builtins") }
    mergeServiceFiles()
}

publishing {
    publications {
        create<MavenPublication>("plugin") {
            artifactId = "eco-plugin"
            artifact(tasks.shadowJar) { classifier = null }
        }
    }
    repositories { maven { name = "localPlugins"; url = uri(localPluginRepoDir) } }
}

tasks.register<Copy>("distribute") {
    description = "将已构建的插件与 API 复制到本机依赖目录"
    dependsOn(tasks.shadowJar, ":eco-api:jar")
    from(tasks.shadowJar)
    from(project(":eco-api").tasks.named("jar"))
    into(externalPluginLibDir)
}
