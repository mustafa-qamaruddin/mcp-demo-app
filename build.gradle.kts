plugins {
    java
    id("io.quarkus")
}

repositories {
    mavenCentral()
    mavenLocal()
}

val quarkusPlatformGroupId: String by project
val quarkusPlatformArtifactId: String by project
val quarkusPlatformVersion: String by project

dependencies {
    implementation(enforcedPlatform("${quarkusPlatformGroupId}:${quarkusPlatformArtifactId}:${quarkusPlatformVersion}"))
    implementation("io.quarkus:quarkus-arc")
    implementation("io.quarkus:quarkus-rest")
    testImplementation("io.quarkus:quarkus-junit")
    testImplementation("io.rest-assured:rest-assured")
    // project dependencies
    implementation("io.modelcontextprotocol.sdk:mcp:1.1.2")
    implementation("org.jboss.slf4j:slf4j-jboss-logmanager")
    compileOnly("jakarta.servlet:jakarta.servlet-api:6.1.0")
    implementation("org.eclipse.jetty:jetty-server:12.1.9")
    implementation("org.eclipse.jetty.ee10:jetty-ee10-servlet:12.1.9")
    implementation("io.quarkus:quarkus-undertow:3.35.3")
}

group = "org.mqubits"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
    options.compilerArgs.add("-parameters")
}
