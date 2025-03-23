package com.task.pjtredis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestConstructor
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedisListCollectionTest(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    private val key = "myList"

    @Test
    @DisplayName("리스트에 데이터를 추가하고 조회할 수 있다")
    fun addAndRetrieveList() {
        redisTemplate.delete(key)

        redisTemplate.opsForList().leftPush(key, "A")
        redisTemplate.opsForList().leftPush(key, "B")
        redisTemplate.opsForList().leftPush(key, "C")

        val values = redisTemplate.opsForList().range(key, 0, -1)
        assertThat(values).containsExactly("C", "B", "A")
    }

    @Test
    @DisplayName("리스트에서 특정 위치의 데이터를 조회할 수 있다")
    fun getElementAtIndex() {
        redisTemplate.delete(key)
        redisTemplate.opsForList().rightPushAll(key, "A", "B", "C")
        val value = redisTemplate.opsForList().index(key, 1)

        assertThat(value).isEqualTo("B")
    }

    @Test
    @DisplayName("리스트에서 데이터를 제거할 수 있다")
    fun removeElementFromList() {
        redisTemplate.delete(key)
        redisTemplate.opsForList().rightPushAll(key, "A", "B", "C", "B", "D")
        redisTemplate.opsForList().remove(key, 1, "B")

        val values = redisTemplate.opsForList().range(key, 0, -1)
        assertThat(values).containsExactly("A", "C", "B", "D")
    }

    @Test
    @DisplayName("리스트의 길이를 확인할 수 있다")
    fun getListSize() {
        redisTemplate.delete(key)
        redisTemplate.opsForList().rightPushAll(key, "A", "B", "C")
        val size = redisTemplate.opsForList().size(key)

        assertThat(size).isEqualTo(3)
    }

    @Test
    @DisplayName("리스트에서 가장 앞 또는 뒤의 데이터를 꺼낼 수 있다")
    fun popElementFromList() {
        redisTemplate.delete(key)
        redisTemplate.opsForList().rightPushAll(key, "A", "B", "C")

        val leftPop = redisTemplate.opsForList().leftPop(key)
        val rightPop = redisTemplate.opsForList().rightPop(key)

        assertThat(leftPop).isEqualTo("A")
        assertThat(rightPop).isEqualTo("C")
    }

}
