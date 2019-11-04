import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "io.arct"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(fileTree(mapOf("dir" to "../libs", "include" to listOf("*.jar"))))

    implementation(kotlin("stdlib-jdk8"))
    compile(kotlin("reflect"))

    implementation(project(":"))

    api("com.squareup:kotlinpoet:1.4.2")
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}