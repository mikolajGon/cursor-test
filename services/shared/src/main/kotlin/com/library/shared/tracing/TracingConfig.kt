package com.library.shared.tracing

import io.ktor.server.application.*
import io.ktor.util.*
import io.opentelemetry.api.OpenTelemetry
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.Tracer
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.context.propagation.ContextPropagators
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.instrumentation.ktor.v2_0.server.KtorServerTracing
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.semconv.ResourceAttributes

object TracingConfig {
        val TRACER_KEY = AttributeKey<Tracer>("tracer_key")

        fun initializeTracing(serviceName: String): OpenTelemetry {
                val spanExporter =
                        OtlpGrpcSpanExporter.builder()
                                .setEndpoint(
                                        System.getenv("OTEL_EXPORTER_OTLP_ENDPOINT")
                                                ?: "http://localhost:4317"
                                )
                                .build()

                val resource =
                        Resource.getDefault()
                                .merge(
                                        Resource.create(
                                                Attributes.of(
                                                        ResourceAttributes.SERVICE_NAME,
                                                        serviceName,
                                                        ResourceAttributes.SERVICE_VERSION,
                                                        "1.0"
                                                )
                                        )
                                )

                val tracerProvider =
                        SdkTracerProvider.builder()
                                .addSpanProcessor(BatchSpanProcessor.builder(spanExporter).build())
                                .setResource(resource)
                                .build()

                return OpenTelemetrySdk.builder()
                        .setTracerProvider(tracerProvider)
                        .setPropagators(
                                ContextPropagators.create(W3CTraceContextPropagator.getInstance())
                        )
                        .build()
        }
}

fun Application.configureTracing(openTelemetry: OpenTelemetry) {
        install(KtorServerTracing) { setOpenTelemetry(openTelemetry) }
        attributes.put(TracingConfig.TRACER_KEY, openTelemetry.getTracer("default"))
}
