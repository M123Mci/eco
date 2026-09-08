plugins { id("io.papermc.paperweight.userdev") }
dependencies {
    paperweight.paperDevBundle(rootProject.property("paperVersion").toString())
}
tasks.shadowJar { duplicatesStrategy = DuplicatesStrategy.EXCLUDE }
