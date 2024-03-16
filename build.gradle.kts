plugins {
	java
	id("org.springframework.boot") version "3.2.1"
	id("io.spring.dependency-management") version "1.1.4"
}

group = "co.uk.pbnj"
version = ""
val recordBuilderVersion = "39"

java {
	sourceCompatibility = JavaVersion.VERSION_19
}

repositories {
	mavenCentral()
}

dependencies {
	annotationProcessor("org.springframework.boot:spring-boot-configuration-processor")
	annotationProcessor("io.soabase.record-builder:record-builder-processor:$recordBuilderVersion")

	implementation("org.springframework.boot:spring-boot-starter-webflux")
	implementation("com.google.guava:guava:33.0.0-jre")
	implementation("org.apache.commons:commons-lang3:3.14.0")

	compileOnly("io.soabase.record-builder:record-builder-core:$recordBuilderVersion")

	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testImplementation("io.projectreactor:reactor-test")
	testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
}

tasks.withType<Test> {
	useJUnitPlatform()
}
