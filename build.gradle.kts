plugins {
    id("java-library")
    id("maven-publish")
}

group = "io.github.cichlidmc"
version = "2.0.1"

repositories {
    mavenLocal()
}

dependencies {
    api("io.github.cichlidmc:TinyJson:1.0.1")
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
