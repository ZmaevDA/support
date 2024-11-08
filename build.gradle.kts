import org.springframework.boot.gradle.tasks.bundling.BootJar

plugins {
    java
    id("org.springframework.boot") version "3.3.1"
    id("io.spring.dependency-management") version "1.1.5"
    id("java-test-fixtures")
    id("org.sonarqube") version "5.1.0.4882"
    id("jacoco")
}

sonar {
    properties {
        property("sonar.projectName", "support")
        property("sonar.projectKey", "ru.zmaev.support")
        property("sonar.host.url", project.findProperty("sonar.url") as String? ?: System.getenv("SONAR_URL"))
        property("sonar.token", project.findProperty("sonar.token") as String? ?: System.getenv("SONAR_TOKEN"))
        property("sonar.junit.reportPaths", "build/test-results/test")
    }
}

group = "ru.zmaev"
version = "1.0-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    create("agent")
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
    maven {
        url = uri("https://maven.pkg.github.com/ZmaevDA/common-lib")
        credentials {
            username = project.findProperty("gpr.user") as String? ?: System.getenv("USERNAME")
            password = project.findProperty("gpr.token") as String? ?: System.getenv("TOKEN")
        }
    }
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.mockito:mockito-core:")
    testImplementation("org.mockito:mockito-junit-jupiter")
    testImplementation("org.junit.jupiter:junit-jupiter-engine")
    testImplementation("org.junit.jupiter:junit-jupiter")
    testImplementation("com.github.dasniko:testcontainers-keycloak:3.3.0")
    testImplementation("org.testcontainers:postgresql:1.19.7")
    testImplementation("org.testcontainers:testcontainers:1.17.6")
    testImplementation("org.testcontainers:junit-jupiter:1.19.7")
    testImplementation("org.assertj:assertj-core:3.25.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test:3.2.5")
    testImplementation("org.springframework.security:spring-security-test:6.2.3")
    testImplementation("io.rest-assured:rest-assured")

    compileOnly("org.projectlombok:lombok")

    runtimeOnly("org.flywaydb:flyway-database-postgresql:10.12.0")
    runtimeOnly("org.postgresql:postgresql")

    annotationProcessor("org.projectlombok:lombok")
    annotationProcessor("org.mapstruct:mapstruct-processor:1.5.5.Final")
    annotationProcessor("org.hibernate:hibernate-jpamodelgen:6.1.7.Final")

    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.flywaydb:flyway-core:10.11.1")
    implementation("org.springframework:spring-webflux:6.1.9")
    implementation("org.springframework:spring-context:6.1.8")
    implementation("org.springframework:spring-jdbc:6.1.8")
    implementation("org.springframework:spring-core:6.1.8")
    implementation("org.springframework:spring-beans:6.1.8")
    implementation("org.springframework.data:spring-data-commons:3.3.0")
    implementation("org.mapstruct:mapstruct:1.5.5.Final")
    implementation("com.auth0:java-jwt:4.3.0")
    implementation("org.json:json:20240205")
    implementation("org.quartz-scheduler:quartz:2.3.2")
    implementation("org.springframework:spring-context-support")

    implementation("io.opentelemetry:opentelemetry-api")
    "agent"("io.opentelemetry.javaagent:opentelemetry-javaagent:2.2.0")

    implementation("ru.zmaev:common-lib:79")
}

dependencyManagement {
    imports {
        mavenBom("io.opentelemetry:opentelemetry-bom:1.36.0")
        mavenBom("io.opentelemetry.instrumentation:opentelemetry-instrumentation-bom-alpha:2.2.0-alpha")
    }
}

tasks.register<Copy>("copyAgent") {
    from(configurations.getByName("agent")) {
        rename("opentelemetry-javaagent-.*\\.jar", "opentelemetry-javaagent.jar")
    }
    into(layout.buildDirectory.dir("agent"))
}

tasks.named<BootJar>("bootJar") {
    dependsOn(tasks.named("copyAgent"))
    archiveFileName.set("app.jar")
}

tasks.test {
    useJUnitPlatform()
    finalizedBy(tasks.jacocoTestReport)
}

jacoco {
    toolVersion = "0.8.8"
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)


    classDirectories.setFrom(
        files(classDirectories.files.flatMap {
            fileTree(it).matching {
                exclude(
                    "ru/zmaev/domain/**",
                    "ru/zmaev/config/**",
                    "ru/zmaev/job/**",
                    "ru/zmaev/repository/**",
                    "ru/zmaev/util/**",
                    "ru/zmaev/validator/**",
                    "ru/zmaev/auth/**"
                )
            }
        })
    )

    reports {
        xml.required.set(true)
        html.required.set(true)
    }
}
