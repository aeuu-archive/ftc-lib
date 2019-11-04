import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"

    id("com.eden.orchidPlugin") version "0.17.6"
    id("org.jlleitschuh.gradle.ktlint") version "9.0.0"
}

group = "io.arct"
version = "0.9.0"

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

    orchidRuntime("io.github.javaeden.orchid:OrchidDocs:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidKotlindoc:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidPluginDocs:0.17.6")
    orchidRuntime("io.github.javaeden.orchid:OrchidSyntaxHighlighter:0.17.6")

    implementation("com.squareup:kotlinpoet:1.4.2")
}

orchid {
    theme = "Editorial"
    version = version
}

tasks.withType<Wrapper> {
    gradleVersion = "5.6.3"
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}
