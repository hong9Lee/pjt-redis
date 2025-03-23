package com.task.pjtredis.domain.campaign

import com.task.pjtredis.domain.EntityId
import java.time.ZonedDateTime

class Campaign(
    val campaignId: EntityId,
    val name: String,
    val maxCount: Int,
    val startDateTime: ZonedDateTime,
    val endDateTime: ZonedDateTime,
) {
    companion object {
        fun create(
            name: String,
            maxCount: Int,
            startDateTime: ZonedDateTime,
            endDateTime: ZonedDateTime,
        ): Campaign {
            return Campaign(
                campaignId = EntityId(),
                name = name,
                maxCount = maxCount,
                startDateTime = startDateTime,
                endDateTime = endDateTime
            )
        }
    }
}
