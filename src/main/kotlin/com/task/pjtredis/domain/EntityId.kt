package com.task.pjtredis.domain

data class EntityId(
    val value: String = UUIDUtil.shortUUID()
)
