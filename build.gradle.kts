plugins {
    kotlin("jvm") version "1.4.0-rc"
    application
    id("com.github.johnrengelman.shadow") version "5.0.0"
}

group = "shinobi9"
version = "1.0-SNAPSHOT"

repositories {
    maven("http://maven.aliyun.com/nexus/content/groups/public")
    maven("http://maven.aliyun.com/nexus/content/repositories/jcenter")
    maven("https://jitpack.io")
    maven("https://dl.bintray.com/kotlin/kotlin-eap")
    mavenCentral()
    jcenter()
    maven("https://kotlin.bintray.com/ktor")
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("io.ktor:ktor-network:1.3.2")
    implementation("ch.qos.logback:logback-classic:1.2.3")
}

application.mainClassName = "shinobi9.krcon.DroneMain"

tasks.shadowJar {
    archiveBaseName.set("krcon")
    archiveVersion.set("")
    archiveClassifier.set("")
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
        kotlinOptions.freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}