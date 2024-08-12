plugins {
    id("java")
    id("maven-publish")
}

group = "io.github.cichlidmc"
version = "2.0-SNAPSHOT"

repositories {
    mavenLocal()
}

dependencies {
    implementation("io.github.cichlidmc:TinyJson:1.0.1")
}

java.withSourcesJar()

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        mavenLocal()
    }
}
