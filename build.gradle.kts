plugins {
    `java`
    `maven-publish`
}

group = "com.luccarieffel"
version = "1.0.0"

repositories {
    mavenCentral()
}

dependencies {
}

publishing {
    publications {
        register("mavenJava", MavenPublication::class) {
            from(components["java"])

            groupId = "com.luccarieffel"
            artifactId = "haybcmd"
            version = "1.0.0"
        }
    }

    repositories {
        maven {
            // Maven Central URL
            url = uri("https://repo.maven.apache.org/maven2")
        }
    }
}

scm {
    connection = "scm:git:git://github.com/SuadoCowboy/HayBCMD.git"
    developerConnection = "scm:git:git@github.com:SuadoCowboy/HayBCMD.git"
    url = "https://github.com/SuadoCowboy/HayBCMD"
}

tasks.register("cleanDist") {
    doLast {
        // Clean the build directory
        file("build").deleteRecursively()
    }
}