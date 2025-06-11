# Grafana Infinite Loop Minimal Reproducer

This is a minimal reproducer of an infinite loop bug in Grafana's trace handling. The Main class is
instrumented with the OpenTelemetry API to create the looping span structure, and Gradle is configured
to both automatically attach the OpenTelemetry Java Agent to the run task and set the environment
variables necessary to upload the generated traces to Grafana. You simply have to provide your Grafana
Cloud URL and auth token.

## Instructions

1) Download the repository
2) Update the two TODO variables in build.gradle.kts
3) `./gradlew run`
4) Attempt to open the generated trace in your Grafana instance