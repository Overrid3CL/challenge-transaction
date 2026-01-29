buildscript {
    repositories {
        mavenCentral()
    }
    dependencies {
        classpath("org.postgresql:postgresql:42.7.2")
		classpath("org.flywaydb:flyway-database-postgresql:10.0.0")
    }
}

plugins {
	java
	id("org.springframework.boot") version "3.5.10"
	id("io.spring.dependency-management") version "1.1.7"
	id("org.flywaydb.flyway") version "10.0.0"
	id("jacoco")
}

group = "alvarado"
version = "0.0.1-SNAPSHOT"
description = "API REST para la gestión de transacciones"

java {
	toolchain {
		languageVersion = JavaLanguageVersion.of(21)
	}
}

configurations {
	compileOnly {
		extendsFrom(configurations.annotationProcessor.get())
	}
}

repositories {
	mavenCentral()
}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter-actuator")
	implementation("org.springframework.boot:spring-boot-starter-data-jpa")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("io.micrometer:micrometer-tracing-bridge-brave")
	implementation("org.flywaydb:flyway-core")
	implementation("org.flywaydb:flyway-database-postgresql")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.8.15")
	compileOnly("org.projectlombok:lombok")
	runtimeOnly("org.postgresql:postgresql")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

tasks.withType<Test> {
	useJUnitPlatform()
	finalizedBy(tasks.jacocoTestReport)
}

// Cargar .env desde la raíz del repo (../.env) para bootRun y tareas Flyway
fun loadEnvFrom(file: java.io.File): Map<String, String> {
	if (!file.exists()) return emptyMap()
	return file.readLines()
		.filter { it.contains("=") && !it.trimStart().startsWith("#") }
		.associate { line ->
			val (key, value) = line.split("=", limit = 2)
			key.trim() to value.trim().removeSurrounding("\"")
		}
}

val envFileForDb = rootProject.layout.projectDirectory.dir("../").file(".env").asFile
val envMap = loadEnvFrom(envFileForDb)

flyway {
	driver = "org.postgresql.Driver"
	url = System.getenv("DB_URL") ?: envMap["DB_URL"] ?: "jdbc:postgresql://localhost:5432/transaction_db"
	user = System.getenv("DB_USER") ?: envMap["DB_USER"] ?: "postgres"
	password = System.getenv("DB_PASSWORD") ?: envMap["DB_PASSWORD"] ?: ""
	cleanDisabled = false
}

tasks.named<org.springframework.boot.gradle.tasks.run.BootRun>("bootRun") {
	if (envFileForDb.exists()) {
		environment(envMap)
	}
}


tasks.jacocoTestReport {
    dependsOn(tasks.test)
    reports {
        xml.required.set(true)
        html.required.set(true) 
    }
    
    classDirectories.setFrom(
        files(classDirectories.files.map {
            fileTree(it) {
                exclude(
                    "**/dto/**",
                    "**/entity/**",
                    "**/config/**",
                    "**/mapper/**",
                    "**/*Application*"
                )
            }
        })
    )
}