package com.example.api.infrastructure

import java.math.BigDecimal
import java.math.BigInteger
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.OffsetDateTime
import java.util.UUID
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonBuilder
import kotlinx.serialization.modules.SerializersModule
import kotlinx.serialization.modules.SerializersModuleBuilder
import java.net.URI
import java.net.URL
import java.util.concurrent.atomic.AtomicBoolean
import java.util.concurrent.atomic.AtomicInteger
import java.util.concurrent.atomic.AtomicLong

object Serializer {
    private var isAdaptersInitialized = false

    @JvmStatic
    val kotlinxSerializationAdapters: SerializersModule by lazy {
        isAdaptersInitialized = true
        SerializersModule {
            contextual(BigDecimal::class,
                _root_ide_package_.com.example.api.infrastructure.BigDecimalAdapter
            )
            contextual(BigInteger::class,
                _root_ide_package_.com.example.api.infrastructure.BigIntegerAdapter
            )
            contextual(LocalDate::class,
                _root_ide_package_.com.example.api.infrastructure.LocalDateAdapter
            )
            contextual(LocalDateTime::class,
                _root_ide_package_.com.example.api.infrastructure.LocalDateTimeAdapter
            )
            contextual(OffsetDateTime::class,
                _root_ide_package_.com.example.api.infrastructure.OffsetDateTimeAdapter
            )
            contextual(UUID::class, _root_ide_package_.com.example.api.infrastructure.UUIDAdapter)
            contextual(AtomicInteger::class,
                _root_ide_package_.com.example.api.infrastructure.AtomicIntegerAdapter
            )
            contextual(AtomicLong::class,
                _root_ide_package_.com.example.api.infrastructure.AtomicLongAdapter
            )
            contextual(AtomicBoolean::class,
                _root_ide_package_.com.example.api.infrastructure.AtomicBooleanAdapter
            )
            contextual(URI::class, _root_ide_package_.com.example.api.infrastructure.URIAdapter)
            contextual(URL::class, _root_ide_package_.com.example.api.infrastructure.URLAdapter)
            contextual(StringBuilder::class,
                _root_ide_package_.com.example.api.infrastructure.StringBuilderAdapter
            )

            apply(kotlinxSerializationAdaptersConfiguration)
        }
    }

    var kotlinxSerializationAdaptersConfiguration: SerializersModuleBuilder.() -> Unit = {}
        set(value) {
            check(!isAdaptersInitialized) {
                "Cannot configure kotlinxSerializationAdaptersConfiguration after kotlinxSerializationAdapters has been initialized."
            }
            field = value
        }

    private var isJsonInitialized = false

    @JvmStatic
    val kotlinxSerializationJson: Json by lazy {
        isJsonInitialized = true
        Json {
            serializersModule = kotlinxSerializationAdapters
            encodeDefaults = true
            ignoreUnknownKeys = true
            isLenient = true

            apply(kotlinxSerializationJsonConfiguration)
        }
    }

    var kotlinxSerializationJsonConfiguration: JsonBuilder.() -> Unit = {}
        set(value) {
            check(!isJsonInitialized) {
                "Cannot configure kotlinxSerializationJsonConfiguration after kotlinxSerializationJson has been initialized."
            }
            field = value
        }
}
