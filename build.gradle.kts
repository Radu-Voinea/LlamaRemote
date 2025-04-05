plugins {
    java
    `java-library`
}

var __version = "1.0.0"
var __group = "com.crazyllama"

version = __version
group = __group


repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(21)
    }
}

repositories {
    applyRepositories()
}

dependencies {
    applyDependencies(project)
}

fun RepositoryHandler.applyRepositories() {
    mavenCentral()
    maven("https://repo.raduvoinea.com/repository/maven-releases/")
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://maven.fabricmc.net/")
    maven("https://jitpack.io/")
}


fun DependencyHandlerScope.applyDependencies(currentProject: Project) {
    // Annotations
    implementation("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
    implementation("org.jetbrains:annotations:24.1.0")
    annotationProcessor("org.jetbrains:annotations:24.1.0")

    // Utils
    api("com.google.code.gson:gson:2.11.0")

    if (currentProject.path != ":common") {
        api(project(":common"))
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "java-library")
    apply(plugin = "maven-publish")

    version = __version
    group = __group

    repositories {
        applyRepositories()
    }

    dependencies {
        applyDependencies(project)
    }

    tasks {
        java {
            sourceCompatibility = JavaVersion.VERSION_21
            targetCompatibility = JavaVersion.VERSION_21
        }

        test {
            useJUnitPlatform()
            java {
                sourceCompatibility = JavaVersion.VERSION_21
                targetCompatibility = JavaVersion.VERSION_21
            }
        }
    }
}

