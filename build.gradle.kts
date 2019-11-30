import java.util.Properties
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.60"

    id("com.eden.orchidPlugin") version "0.17.6"
    id("org.jlleitschuh.gradle.ktlint") version "9.0.0"
}

allprojects {
    group = "io.arct"
    version = "0.9.1"
}

val properties: Properties = Properties()

properties.load(project.rootProject.file("local.properties").inputStream())

repositories {
    mavenCentral()
    jcenter()

    maven("https://kotlin.bintray.com/kotlinx/")

    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(fileTree(mapOf("dir" to "libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    implementation("com.squareup:kotlinpoet:1.4.4")

    orchidRuntime("io.github.javaeden.orchid:OrchidDocs:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidGithub:0.17.6")
}

orchid {
    theme = "Editorial"

    version = version
    baseUrl = "/ftc-lib/"

    githubToken = properties.getProperty("githubToken")
}

tasks.withType<Wrapper> {
    gradleVersion = "5.6.3"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
