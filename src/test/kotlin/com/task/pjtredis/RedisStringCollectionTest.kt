package com.task.pjtredis

import com.fasterxml.jackson.annotation.JsonCreator
import com.fasterxml.jackson.annotation.JsonProperty
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import org.assertj.core.api.Assertions.assertThat
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.context.annotation.Description
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestConstructor
import java.time.Duration
import java.util.Base64
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedisStringCollectionTest(
    private val redisTemplate: RedisTemplate<String, String>,
) {

    @Test
    @Description("key, value 형태의 단순 문자열을 저장할 수 있다.")
    fun saveStringTest() {
        redisTemplate.opsForValue().set("test", "value")
        val value = redisTemplate.opsForValue().get("test")
        assertThat(value).isEqualTo("value")
    }

    @Test
    @Description("여러 개의 key-value를 한 번에 저장하고 조회할 수 있다.")
    fun saveMultipleStringsTest() {
        val data = mapOf(
            "key1" to "value1",
            "key2" to "value2",
            "key3" to "value3"
        )

        redisTemplate.opsForValue().multiSet(data)
        val values = redisTemplate.opsForValue().multiGet(data.keys.toList())

        assertThat(values).containsExactly("value1", "value2", "value3")
    }

    @Test
    @Description("저장된 값을 삭제할 수 있다.")
    fun deleteStringTest() {
        redisTemplate.opsForValue().set("test", "value")
        redisTemplate.delete("test")
        val value = redisTemplate.opsForValue().get("test")
        assertThat(value).isNull()
    }

    @Test
    @Description("Key가 존재하는지 확인할 수 있다.")
    fun existsKeyTest() {
        redisTemplate.opsForValue().set("test", "value")
        val exists = redisTemplate.hasKey("test")
        assertThat(exists).isTrue()
    }

    @Test
    @Description("Key에 TTL을 설정하고 확인할 수 있다.")
    fun keyExpireTest() {
        redisTemplate.opsForValue().set("test", "value")
        redisTemplate.expire("test", Duration.ofSeconds(10))
        val ttl = redisTemplate.getExpire("test")
        assertThat(ttl).isGreaterThan(0)
    }

    @Test
    @Description("Key에 만료 시간을 설정하면서 저장할 수 있다.")
    fun setWithExpireTest() {
        redisTemplate.opsForValue().set("temp", "expire_value", Duration.ofMillis(1))
        Thread.sleep(10)
        val value = redisTemplate.opsForValue().get("temp")
        assertThat(value).isNull()
    }

    @Test
    @Description("숫자 값을 증가 및 감소시킬 수 있다.")
    fun incrementAndDecrementTest() {
        redisTemplate.opsForValue().set("counter", "10")
        redisTemplate.opsForValue().increment("counter")
        redisTemplate.opsForValue().decrement("counter", 2)

        val value = redisTemplate.opsForValue().get("counter")
        assertThat(value).isEqualTo("9")
    }

    @Test
    @Description("기존 값을 새로운 값으로 변경하면서 이전 값을 반환받을 수 있다.")
    fun getSetTest() {
        redisTemplate.opsForValue().set("test", "old_value")
        val oldValue = redisTemplate.opsForValue().getAndSet("test", "new_value")

        assertThat(oldValue).isEqualTo("old_value")
        assertThat(redisTemplate.opsForValue().get("test")).isEqualTo("new_value")
    }


    @Test
    @Description("기존 문자열에 새로운 문자열을 추가할 수 있다.")
    fun appendStringTest() {
        redisTemplate.opsForValue().set("test", "Hello")
        redisTemplate.opsForValue().append("test", " World")

        val value = redisTemplate.opsForValue().get("test")
        assertThat(value).isEqualTo("Hello World")
    }

    @Test
    @Description("문자열 길이를 확인할 수 있다.")
    fun stringLengthTest() {
        val value = "Hello, Redis!"
        redisTemplate.opsForValue().set("test", value)
        val length = redisTemplate.opsForValue().size("test")

        assertThat(length).isEqualTo(value.length.toLong())
    }

    data class User @JsonCreator constructor(
        @JsonProperty("id") val id: Long,
        @JsonProperty("name") val name: String
    )
    private val objectMapper = ObjectMapper()
    @Test
    @Description("JSON 데이터를 Redis에 저장하고 조회할 수 있다")
    fun jsonTest() {
        val key = "user:1"
        val user = User(1, "Alice~")

        val jsonValue = objectMapper.writeValueAsString(user)
        redisTemplate.opsForValue().set(key, jsonValue)

        val retrievedJson = redisTemplate.opsForValue().get(key)
        val retrievedUser: User = objectMapper.readValue(retrievedJson!!)

        assertThat(retrievedUser.id).isEqualTo(1)
        assertThat(retrievedUser.name).isEqualTo("Alice~")
    }

    @Test
    @Description("Binary 데이터를 Base64로 변환하여 Redis에 저장하고 조회할 수 있다")
    fun binaryTest() {
        val key = "binary:data"
        val binaryData = "Hello, Binary Redis!".toByteArray(Charsets.UTF_8)

        val encodedData = Base64.getEncoder().encodeToString(binaryData)
        redisTemplate.opsForValue().set(key, encodedData)

        val retrievedEncodedData = redisTemplate.opsForValue().get(key)
        val decodedData = Base64.getDecoder().decode(retrievedEncodedData)

        assertThat(String(decodedData, Charsets.UTF_8)).isEqualTo("Hello, Binary Redis!")
    }

}
