package com.example;

import io.opentelemetry.api.GlobalOpenTelemetry;
import io.opentelemetry.api.OpenTelemetry;
import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import io.opentelemetry.context.Scope;

@SuppressWarnings("resource")
public class Main {
    public static void main(String[] args) throws InterruptedException {
        System.out.println("Beginning generation of infinite loop traces.");
        OpenTelemetry otel = GlobalOpenTelemetry.get();
        Tracer tracer = otel.getTracer("infinite-loop-producer");
        System.out.println("Starting client span. Span length will be 0.5 seconds.");
        Span clientSpan = tracer.spanBuilder("client").startSpan();
        System.out.println("Starting root span, adding client <-> root link. Span length will be 3 seconds.");
        Span rootSpan = tracer.spanBuilder("root").addLink(clientSpan.getSpanContext()).startSpan();
        clientSpan.addLink(rootSpan.getSpanContext());
        Scope rootScope = rootSpan.makeCurrent();
        Thread.sleep(500);
        System.out.println("Ending client span.");
        clientSpan.end();
        Thread.sleep(500);
        System.out.println("Starting intermediate span, child of root span. Span length will be 2 seconds.");
        Span intermediateSpan = tracer.spanBuilder("intermediate").startSpan();
        Scope intermediateScope = intermediateSpan.makeCurrent();
        Thread.sleep(1000);
        System.out.println("Starting child span, adding child <-> root link. Span length will be 1 second.");
        Span childSpan = tracer.spanBuilder("child").addLink(rootSpan.getSpanContext()).startSpan();
        rootSpan.addLink(childSpan.getSpanContext());
        Thread.sleep(1000);
        System.out.println("Ending child span");
        childSpan.end();
        System.out.println("Ending intermediate span");
        intermediateSpan.end();
        intermediateScope.close();
        System.out.println("Ending root span");
        rootSpan.end();
        rootScope.close();
        System.out.println("Traces generated. Trace ID of infinite loop: " + rootSpan.getSpanContext().getTraceId());
    }
}