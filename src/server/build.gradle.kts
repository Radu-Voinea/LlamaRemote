plugins {
    java
    id("org.springframework.boot") version "3.2.5"
    id("io.spring.dependency-management") version "1.1.4"
}

group = "com.crazyllama"
version = "1.0.0"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion.set(JavaLanguageVersion.of(21))
    }
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.projectlombok:lombok")
    implementation("org.springframework.boot:spring-boot-starter-web")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")

    implementation("org.hibernate:hibernate-core:6.6.11.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.6.11.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.2.Final")

    implementation("org.jetbrains:annotations:24.1.0")
    implementation("com.google.code.gson:gson:2.11.0")
}
