//import magik.createGithubPublication
//import magik.github

plugins {
    // Apply the org.jetbrains.kotlin.jvm Plugin to add support for Kotlin.
    kotlin("jvm") version embeddedKotlinVersion

    // Apply the java-library plugin for API and implementation separation.
    `java-library`

//    id("elect86.magik") version "0.2.0"
    `maven-publish`

    application
}

version = "0.0.1"
group = "elect86"

repositories {
    // Use Maven Central for resolving dependencies.
    mavenCentral()
}

application {
    //    mainClass.set("slurm.MiscKt")
    //    mainClass.set("slurm.NodesKt")
    //    mainClass.set("slurm.SqueueKt")
}

dependencies {

//    implementation("com.github.ajalt.mordant:mordant:2.0.0-beta2")
//    implementation("com.github.ajalt.mordant:mordant-jvm:2.0.0-beta2")

    // Align versions of all Kotlin components
    implementation(platform(kotlin("bom", embeddedKotlinVersion)))

    // Use the Kotlin JDK 8 standard library.
    implementation(kotlin("stdlib-jdk8"))

    // Use the Kotlin test library.
    testImplementation(kotlin("test"))

    // Use the Kotlin JUnit integration.
    //    testImplementation("org.jetbrains.kotlin:kotlin-test-junit")
}

tasks {
    test { useJUnitPlatform() }
}
//
//publishing {
//    publications {
//        createGithubPublication {
//            from(components["java"])
//            //    artifact(sourceJar)
//        }
//        repositories {
//            github {
//                domain = "kotlin-graphics/mary" // aka https://github.com/kotlin-graphics/mary
//            }
//        }
//    }
//}