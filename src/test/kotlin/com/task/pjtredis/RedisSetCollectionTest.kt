package com.task.pjtredis

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.test.context.TestConstructor
import java.util.concurrent.CountDownLatch
import java.util.concurrent.Executors
import kotlin.test.Test

@SpringBootTest
@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
class RedisSetCollectionTest(
    private val redisTemplate: RedisTemplate<String, String>,
) {
    private val key = "mySet"

    @Test
    @DisplayName("데이터를 추가하고 조회할 수 있다")
    fun addAndRetrieveSet() {
        redisTemplate.delete(key)
        redisTemplate.opsForSet().add(key, "A", "B", "C", "A")
        val values = redisTemplate.opsForSet().members(key)

        assertThat(values).containsExactlyInAnyOrder("A", "C", "B")
    }

    @Test
    @DisplayName("특정 값이 존재하는지 확인할 수 있다")
    fun checkElementExistsInSet() {
        redisTemplate.delete(key)
        redisTemplate.opsForSet().add(key, "A", "B", "C")
        val exists = redisTemplate.opsForSet().isMember(key, "B")

        assertThat(exists).isTrue()
    }

    @Test
    @DisplayName("데이터를 제거할 수 있다")
    fun removeElementFromSet() {
        redisTemplate.delete(key)
        redisTemplate.opsForSet().add(key, "A", "B", "C")
        redisTemplate.opsForSet().remove(key, "B")
        val values = redisTemplate.opsForSet().members(key)

        assertThat(values).containsExactlyInAnyOrder("A", "C")
    }

    @Test
    @DisplayName("데이터 크기를 확인할 수 있다")
    fun getSetSize() {
        redisTemplate.delete(key)
        redisTemplate.opsForSet().add(key, "A", "B", "C")
        val size = redisTemplate.opsForSet().size(key)

        assertThat(size).isEqualTo(3)
    }

    @Test
    @DisplayName("랜덤한 값을 가져올 수 있다")
    fun popRandomElementFromSet() {
        redisTemplate.delete(key)
        redisTemplate.opsForSet().add(key, "A", "B", "C")
        val randomValue = redisTemplate.opsForSet().pop(key)

        assertThat(randomValue).isNotNull()
    }

    @Test
    @DisplayName("두 개의 set에서 교집합을 구할 수 있다")
    fun getSetIntersection() {
        redisTemplate.delete("set1")
        redisTemplate.delete("set2")
        redisTemplate.opsForSet().add("set1", "A", "B", "C")
        redisTemplate.opsForSet().add("set2", "B", "C", "D")
        val intersection = redisTemplate.opsForSet().intersect("set1", "set2")

        assertThat(intersection).containsExactlyInAnyOrder("B", "C")
    }

    @Test
    @DisplayName("두 개의 set에서 합집합을 구할 수 있다")
    fun getSetUnion() {
        redisTemplate.delete("set1")
        redisTemplate.delete("set2")

        redisTemplate.opsForSet().add("set1", "A", "B", "C")
        redisTemplate.opsForSet().add("set2", "B", "C", "D")

        val union = redisTemplate.opsForSet().union("set1", "set2")

        assertThat(union).containsExactlyInAnyOrder("A", "B", "C", "D")
    }

    @Test
    @DisplayName("두 개의 set에서 차집합을 구할 수 있다")
    fun getSetDifference() {
        redisTemplate.delete("set1")
        redisTemplate.delete("set2")

        redisTemplate.opsForSet().add("set1", "A", "B", "C")
        redisTemplate.opsForSet().add("set2", "B", "C", "D")

        val difference = redisTemplate.opsForSet().difference("set1", "set2")

        assertThat(difference).containsExactlyInAnyOrder("A")
    }

    @Test
    @DisplayName("여러 스레드에서 동시에 SADD 실행 시 중복 없이 저장되는지 확인")
    fun testConcurrentSADD() {
        redisTemplate.delete(key)

        val threadCount = 100
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 1..threadCount) {
            executorService.execute {
                redisTemplate.opsForSet().add(key, "VALUE-$i")
                latch.countDown()
            }
        }

        latch.await()
        executorService.shutdown()

        val storedValues = redisTemplate.opsForSet().members(key)
        assertThat(storedValues?.size).isEqualTo(100)
    }

    @Test
    @DisplayName("여러 스레드에서 SUNIONSTORE 실행 시 데이터 정합성이 유지되는지 확인")
    fun testConcurrentSUNIONSTORE() {
        redisTemplate.delete("setA")
        redisTemplate.delete("setB")
        redisTemplate.delete("resultSet")

        redisTemplate.opsForSet().add("setA", "A1", "A2", "A3")
        redisTemplate.opsForSet().add("setB", "B1", "B2", "B3")

        val threadCount = 10
        val executorService = Executors.newFixedThreadPool(threadCount)
        val latch = CountDownLatch(threadCount)

        for (i in 1..threadCount) {
            executorService.execute {
                redisTemplate.opsForSet().unionAndStore("setA", "setB", "resultSet")
                latch.countDown()
            }
        }

        latch.await()
        executorService.shutdown()

        val finalResult = redisTemplate.opsForSet().members("resultSet")
        assertThat(finalResult).containsExactlyInAnyOrder("A1", "A2", "A3", "B1", "B2", "B3")
    }


}
