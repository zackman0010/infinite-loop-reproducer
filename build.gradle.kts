plugins {
    java
    application
    id("com.ryandens.javaagent-application").version("0.8.0")
}

group = "com.example"
version = "1.0-SNAPSHOT"
description = "Minimal reproducer of a trace that will cause an infinite loop browser crash in Grafana"

repositories {
    mavenCentral()
}

dependencies {
    implementation("io.opentelemetry:opentelemetry-api:1.42.1")
    javaagent("io.opentelemetry.javaagent:opentelemetry-javaagent:2.8.0")
}

application {
    mainClass = "com.example.Main"
}

//TODO - Populate these with values for your stack
val grafanaOtlpUrl = "TODO"
val grafanaAuthToken = "TODO" //Base64 encoded Basic auth token

(tasks.run) {
    environment["OTEL_SERVICE_NAME"] = "infinite-loop-producer"
    environment["OTEL_TRACES_EXPORTER"] = "otlp,console"
    environment["OTEL_METRICS_EXPORTER"] = "none"
    environment["OTEL_LOGS_EXPORTER"] = "none"
    environment["OTEL_EXPORTER_OTLP_PROTOCOL"] = "http/protobuf"
    environment["OTEL_EXPORTER_OTLP_ENDPOINT"] = grafanaOtlpUrl
    environment["OTEL_EXPORTER_OTLP_HEADERS"] = "Authorization=Basic $grafanaAuthToken"
}
