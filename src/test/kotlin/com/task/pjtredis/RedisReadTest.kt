package com.task.pjtredis

import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertTrue
import redis.clients.jedis.Jedis
import java.util.concurrent.Executors
import kotlin.system.measureTimeMillis
import kotlin.test.Test

class RedisReadTest {
    private val redisHost = "localhost"
    private val redisPort = 6379
    private val key = "test-key"

    @Test
    fun `Redis 멀티스레드 GET 요청 성능 테스트`() {
        val redis = Jedis(redisHost, redisPort)

        // 1. 테스트용 키 저장
        redis.set(key, "Redis Performance Test")
        redis.close()

        val threadCount = 100 // 동시에 실행할 스레드 개수
        val requestPerThread = 1000 // 각 스레드가 실행할 요청 수
        val executor = Executors.newFixedThreadPool(threadCount)

        val elapsedTime = measureTimeMillis {
            repeat(threadCount) {
                executor.execute {
                    val redisThread = Jedis(redisHost, redisPort)
                    repeat(requestPerThread) {
                        assertNotNull(redisThread.get(key)) // 키가 존재하는지 검증
                    }
                    redisThread.close()
                }
            }
            executor.shutdown()
            while (!executor.isTerminated) { }
        }

        println("총 ${threadCount * requestPerThread} GET 요청 완료!")
        println("실행 시간: $elapsedTime ms")

        assertTrue(elapsedTime > 0)
    }
}
