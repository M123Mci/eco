import org.gradle.api.attributes.java.TargetJvmVersion
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    id("io.papermc.paperweight.userdev")
}

group = "com.willfp"
version = rootProject.version

dependencies {
    implementation(project(":eco-core:core-nms:common"))
    implementation(project(":eco-core:core-nms:modern"))
    implementation(project(":eco-core:core-nms:v1_21_6", configuration = "shadow"))
    paperweight.paperDevBundle("26.1.2.build.19-alpha")
}

tasks {
    build {
        dependsOn(shadowJar)
    }

    shadowJar {
        relocate(
            "com.willfp.eco.internal.spigot.proxy.v1_21_6",
            "com.willfp.eco.internal.spigot.proxy.v1_21_11"
        )
        relocate(
            "com.willfp.eco.internal.spigot.proxy.common",
            "com.willfp.eco.internal.spigot.proxy.v1_21_11.common"
        )

        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/PlayerHandler*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/TPS*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/CommonsInitializer*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/SNBTConverter*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/PacketHandler*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/packet/NewItemsPacketOpenWindowMerchant*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/packet/NewItemsPacketSetCreativeSlot*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/packet/NewItemsPacketWindowItems*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/packet/PacketContainerClick*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/v1_21_6/packet/PacketSetCursorItem*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/common/packet/display/PacketHeldItemSlot*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/common/packet/display/PacketSetSlot*.class")
        exclude("com/willfp/eco/internal/spigot/proxy/common/packet/display/PacketWindowItems*.class")

        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }

    compileKotlin {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_25)
        }
    }

    withType<JavaCompile>().configureEach {
        options.release.set(25)
    }
}

configurations.matching {
    it.name == JavaPlugin.COMPILE_CLASSPATH_CONFIGURATION_NAME ||
        it.name == JavaPlugin.RUNTIME_CLASSPATH_CONFIGURATION_NAME
}.configureEach {
    attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, 25)
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}
