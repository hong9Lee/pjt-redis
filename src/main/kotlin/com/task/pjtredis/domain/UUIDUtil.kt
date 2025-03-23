package com.task.pjtredis.domain

import java.nio.ByteBuffer
import java.util.Base64
import java.util.UUID

/**
 * UUID 관련 유틸리티
 */
object UUIDUtil {

    private val encoder: Base64.Encoder = Base64.getUrlEncoder().withoutPadding()
    private val decoder: Base64.Decoder = Base64.getUrlDecoder()

    /**
     * 짧은 UUID 문자열을 얻는다.
     *
     * @return short UUID String
     */
    fun shortUUID(): String {
        val uuid = UUID.randomUUID()
        return shortUUID(uuid)
    }

    /**
     * 주어진 UUID의 짧은 UUID 문자열을 얻는다.
     *
     * @param uuid 원본 UUID
     * @return 짧은 형식으로 변환한 문자열
     */
    fun shortUUID(uuid: UUID?): String {
        requireNotNull(uuid) { "uuid must not be null" }

        val bb = ByteBuffer.allocate(16)
        bb.putLong(uuid.mostSignificantBits)
        bb.putLong(uuid.leastSignificantBits)

        return encoder.encodeToString(bb.array())
    }
}
