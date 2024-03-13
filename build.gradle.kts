plugins {
    `java`
    `maven-publish`
}

val group = "com.luccarieffel"
val version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])

            // Configure the publication
            groupId = group
            artifactId = "haybcmd"
            this.version = version

            pom {
                // Specify SCM information
                scm {
                    connection.set("scm:git:git://github.com/SuadoCowboy/HayBCMD.git")
                    developerConnection.set("scm:git:git@github.com:SuadoCowboy/HayBCMD.git")
                    url.set("https://github.com/SuadoCowboy/HayBCMD")
                }
            }
        }
    }

    repositories {
        maven {
            url = uri("https://repo.maven.apache.org/maven2")
            credentials {
                username = project.findProperty("mavenUsername")?.toString()
                password = project.findProperty("mavenPassword")?.toString()
            }
        }
    }
}

tasks.register("cleanDist") {
    doLast {
        file("build").deleteRecursively()
    }
}