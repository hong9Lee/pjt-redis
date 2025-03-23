package com.task.pjtredis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.HashOperations
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedisHashCollectionTest(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    private val hashKey = "user:1001"

    @Test
    @DisplayName("해시에 필드를 추가하고 조회할 수 있다")
    fun addAndRetrieveHash() {
        redisTemplate.delete(hashKey)

        val hashOps: HashOperations<String, String, String> = redisTemplate.opsForHash()

        hashOps.put(hashKey, "name", "Alice")
        hashOps.put(hashKey, "age", "25")
        hashOps.put(hashKey, "city", "Seoul")

        val name = hashOps.get(hashKey, "name")
        val age = hashOps.get(hashKey, "age")

        assertThat(name).isEqualTo("Alice")
        assertThat(age).isEqualTo("25")
    }

    @Test
    @DisplayName("해시에서 특정 필드를 삭제할 수 있다")
    fun removeFieldFromHash() {
        redisTemplate.delete(hashKey)

        val hashOps: HashOperations<String, String, String> = redisTemplate.opsForHash()

        hashOps.put(hashKey, "name", "Alice")
        hashOps.delete(hashKey, "name")

        val name = hashOps.get(hashKey, "name")

        assertThat(name).isNull()
    }

    @Test
    @DisplayName("해시에서 모든 필드를 조회할 수 있다")
    fun getAllFieldsFromHash() {
        redisTemplate.delete(hashKey)

        val hashOps: HashOperations<String, String, String> = redisTemplate.opsForHash()

        hashOps.put(hashKey, "name", "Alice")
        hashOps.put(hashKey, "age", "25")
        hashOps.put(hashKey, "city", "Seoul")

        val entries = hashOps.entries(hashKey)

        assertThat(entries).containsEntry("name", "Alice")
        assertThat(entries).containsEntry("age", "25")
        assertThat(entries).containsEntry("city", "Seoul")
    }

    @Test
    @DisplayName("해시 필드의 존재 여부를 확인할 수 있다")
    fun checkIfHashFieldExists() {
        redisTemplate.delete(hashKey)

        val hashOps: HashOperations<String, String, String> = redisTemplate.opsForHash()

        hashOps.put(hashKey, "name", "Alice")

        val exists = hashOps.hasKey(hashKey, "name")
        val notExists = hashOps.hasKey(hashKey, "age")

        assertThat(exists).isTrue()
        assertThat(notExists).isFalse()
    }

    @Test
    @DisplayName("해시 필드의 값을 증가시킬 수 있다")
    fun incrementHashField() {
        redisTemplate.delete(hashKey)

        val hashOps: HashOperations<String, String, Long> = redisTemplate.opsForHash()

        hashOps.put(hashKey, "score", 10)
        hashOps.increment(hashKey, "score", 5)

        val updatedScore = hashOps.get(hashKey, "score")
        assertThat(updatedScore).isEqualTo("15")
    }


}
