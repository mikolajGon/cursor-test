plugins {
    kotlin("jvm")
}

repositories {
    mavenCentral()
}

dependencies {
    val openTelemetryVersion = "1.32.0"
    val semconvVersion = "1.21.0-alpha"
    val ktorVersion = "2.3.7"
    
    implementation(platform("io.opentelemetry:opentelemetry-bom:$openTelemetryVersion"))
    implementation("io.opentelemetry:opentelemetry-api")
    implementation("io.opentelemetry:opentelemetry-sdk")
    implementation("io.opentelemetry:opentelemetry-exporter-otlp")
    implementation("io.opentelemetry.semconv:opentelemetry-semconv:$semconvVersion")
    implementation("io.opentelemetry.instrumentation:opentelemetry-ktor-2.0:2.0.0-alpha")
    implementation("io.ktor:ktor-server-core:$ktorVersion")
} 