plugins {
	id("fabric-loom") version("1.10.5")
}

val MOD_VERSION = "1.0.0"
val FABRIC_VERSION = "0.19.3"
val MINECRAFT_VERSION = "1.20.1"
val RELEASE_TAG = "mc${MINECRAFT_VERSION}-${MOD_VERSION}"

dependencies {
	minecraft("com.mojang:minecraft:${MINECRAFT_VERSION}")
	mappings(loom.officialMojangMappings())
	
	modImplementation("net.fabricmc:fabric-loader:${FABRIC_VERSION}")
}

loom {
  mixin {
    useLegacyMixinAp = false
  }
  
  runs {
    named("client") {
      client()
      configName = "Fabric/Client"
      appendProjectPathToConfigName = false
      ideConfigGenerated(true)
      runDir("run")
    }
  }
}

tasks.processResources {
	inputs.property("version", RELEASE_TAG)

	filesMatching("fabric.mod.json") {
		expand("version" to MOD_VERSION)
	}
}

tasks.withType<JavaCompile>().configureEach {
	options.release = 17
}

java {
	sourceCompatibility = JavaVersion.VERSION_17
	targetCompatibility = JavaVersion.VERSION_17
}

tasks.jar {
	val projectName = project.name
	inputs.property("projectName", projectName)

  from(rootDir.resolve("LICENSE.txt"))
}
