val group = "cn.cyanbukkit.plant"
val version = "0.1"

bukkit {
    name = rootProject.name
    description = " CyanBukkit Template Plugins"
    authors = listOf("NostMC")
    website = "https://cyanbukkit.cn"
    main = "${group}.cyanlib.launcher.CyanPluginLauncher"
    loadBefore = listOf("SiModuleGame")
    depend = listOf("ProtocolLib", "PlaceholderAPI")
}

plugins {
    java
    kotlin("jvm") version "1.9.20"
    id("net.minecrell.plugin-yml.bukkit") version "0.6.0"
}

repositories {
    maven("https://nexus.cyanbukkit.cn/repository/maven-public/")
    maven("https://maven.elmakers.com/repository")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    compileOnly("org.spigotmc:minecraft-server:1.12.2-SNAPSHOT")
    compileOnly("org.bukkit:craftbukkit:1.12.2-R0.1-SNAPSHOT")
    compileOnly("xyz.xenondevs:particle:1.8.4")
    compileOnly("com.comphenix.protocol:ProtocolLib:4.8.0")
    compileOnly(fileTree("libs") { include("*.jar") })
    compileOnly(fileTree("E:\\我的世界互动\\4.种甜菜\\服务端\\plugins\\SiModuleGame-Bukkit-24.5.1.jar"))
    compileOnly("org.apache.commons:commons-lang3:3.12.0")
    // PlaceholderAPI
    compileOnly("me.clip:placeholderapi:2.11.3")
}

kotlin {
    jvmToolchain(8)
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileJava {
        options.encoding = "UTF-8"
    }

    jar {
        archiveFileName.set("${rootProject.name}-${version}.jar")
    }
}
