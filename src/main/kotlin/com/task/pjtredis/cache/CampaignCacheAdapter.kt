package com.task.pjtredis.cache

import org.springframework.data.redis.core.RedisTemplate
import org.redisson.api.RedissonClient
import org.springframework.stereotype.Component
import java.time.Duration
import java.util.concurrent.TimeUnit

@Component
class CampaignCacheAdapter(
    private val redisTemplate: RedisTemplate<String, String>,
    private val redissonClient: RedissonClient
) {

    companion object {
        private const val PARTICIPATION_KEY_PREFIX = "campaign:remaining:"
        private const val PARTICIPATION_LOCK_PREFIX = "campaign:lock:"
    }

    fun saveRemainingParticipation(campaignId: String, count: Long, expireTime: Long) {
        val key = participationKey(campaignId)
        redisTemplate.opsForValue().set(key, count.toString(), expireTime, TimeUnit.SECONDS)
    }

    fun getRemainingParticipation(campaignId: String): Long? {
        val key = participationKey(campaignId)
        return redisTemplate.opsForValue().get(key)?.toLongOrNull()
    }

    fun decrementParticipation(campaignId: String): Long {
        val key = participationKey(campaignId)
        val updated = redisTemplate.opsForValue().decrement(key) ?: -1

        if (updated < 0) {
            redisTemplate.opsForValue().set(key, "0")
        }

        return updated
    }

    fun saveRemainingParticipationIfAbsent(campaignId: String, count: Long, expireTime: Long) {
        val key = participationKey(campaignId)
        redisTemplate.opsForValue().setIfAbsent(key, count.toString(), Duration.ofSeconds(expireTime))
    }


    fun acquireLock(campaignId: String, waitTime: Long, leaseTime: Long): Boolean {
        val lock = redissonClient.getLock(lockKey(campaignId))
        return lock.tryLock(waitTime, leaseTime, TimeUnit.SECONDS)
    }

    fun releaseLock(campaignId: String) {
        val lock = redissonClient.getLock(lockKey(campaignId))
        if (lock.isHeldByCurrentThread) {
            lock.unlock()
        }
    }

    private fun participationKey(campaignId: String): String {
        return "$PARTICIPATION_KEY_PREFIX$campaignId"
    }

    private fun lockKey(campaignId: String): String {
        return "$PARTICIPATION_LOCK_PREFIX$campaignId"
    }
}
