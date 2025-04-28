plugins {
    id("java-library")
    id("maven-publish")
}

group = "fish.cichlidmc"
version = "2.0.2"

repositories {
    maven("https://mvn.devos.one/releases/")
}

dependencies {
    api("fish.cichlidmc:tiny-json:1.2.0")
}

java.withSourcesJar()

publishing {
    publications {
        create<MavenPublication>("mavenJava") {
            from(components["java"])
        }
    }

    repositories {
        listOf("Releases", "Snapshots").forEach {
            maven("https://mvn.devos.one/${it.lowercase()}") {
                name = "devOs$it"
                credentials(PasswordCredentials::class)
            }
        }
    }
}
