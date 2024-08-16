plugins {
    id("java-library")
    id("maven-publish")
}

base.archivesName = "PistonMetaParser"
group = "io.github.cichlidmc"
version = "2.0.2"

repositories {
    maven("https://mvn.devos.one/snapshots/")
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
        maven("https://mvn.devos.one/snapshots") {
            name = "devOS"
            credentials(PasswordCredentials::class)
        }
    }
}
