package com.task.pjtredis

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.autoconfigure.data.redis.RedisProperties
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(RedisProperties::class)
@ConfigurationPropertiesScan
class PjtRedisApplication

fun main(args: Array<String>) {
    runApplication<PjtRedisApplication>(*args)
}
