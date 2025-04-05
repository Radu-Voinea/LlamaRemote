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
    // Spring
    implementation("org.springframework.boot:spring-boot-starter"){
        exclude(group = "org.hibernate", module = "hibernate-core")
    }
    implementation("org.springframework.boot:spring-boot-starter-web"){
        exclude(group = "org.hibernate", module = "hibernate-core")
    }

    // Database
    implementation("org.hibernate:hibernate-core:6.4.4.Final")
    implementation("org.hibernate.orm:hibernate-hikaricp:6.4.4.Final")
    implementation("org.hibernate.validator:hibernate-validator:8.0.2.Final")
    implementation("org.mariadb.jdbc:mariadb-java-client:3.5.2")

    implementation("jakarta.el:jakarta.el-api:6.0.1")
    implementation("jakarta.inject:jakarta.inject-api:2.0.1")
    implementation("com.mchange:c3p0:0.10.2")
    implementation("aopalliance:aopalliance:1.0")
    implementation("org.osgi:org.osgi.framework:1.10.0")
}
