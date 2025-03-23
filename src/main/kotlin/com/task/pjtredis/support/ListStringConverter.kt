package com.task.ad.support

import com.fasterxml.jackson.core.type.TypeReference
import com.task.pjtredis.support.GenericJsonConverter
import io.github.oshai.kotlinlogging.KotlinLogging

private val log = KotlinLogging.logger {}

class ListStringConverter : GenericJsonConverter<List<String>>(
    object : TypeReference<List<String>>() {}
)
