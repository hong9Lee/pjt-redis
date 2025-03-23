package com.task.pjtredis.support

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.github.oshai.kotlinlogging.KotlinLogging
import jakarta.persistence.AttributeConverter
import java.io.IOException

open class GenericJsonConverter<T>(private val valueTypeRef: TypeReference<T>) : AttributeConverter<T, String> {
    private val log = KotlinLogging.logger {}

    private val objectMapper: ObjectMapper = ObjectMapper()

    override fun convertToDatabaseColumn(`object`: T): String {
        return try {
            objectMapper.writeValueAsString(`object`)
        } catch (e: JsonProcessingException) {
            log.error { "Fail to serialize object into Json: $`object` $e" }
            throw RuntimeException(e)
        }
    }

    override fun convertToEntityAttribute(jsonStr: String): T {
        return try {
            objectMapper.readValue(jsonStr, valueTypeRef)
        } catch (e: IOException) {
            log.error { "Fail to deserialize Json into Object: $jsonStr $e" }
            throw RuntimeException(e)
        }
    }
}
