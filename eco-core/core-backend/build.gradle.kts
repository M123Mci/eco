group = "com.willfp"
version = rootProject.version

val paperVersion: String by project

dependencies {
    compileOnly("io.papermc.paper:paper-api:$paperVersion")
    compileOnly("me.clip:placeholderapi:2.12.3")
    compileOnly("net.kyori:adventure-text-minimessage:5.0.1")
    compileOnly("net.kyori:adventure-platform-bukkit:4.4.1")
    compileOnly("org.yaml:snakeyaml:2.5")
    compileOnly("io.hotmoka:toml4j:0.7.3")
}
