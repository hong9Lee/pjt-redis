package com.task.pjtredis.domain.campaign

import com.task.pjtredis.domain.EntityId
import java.time.ZonedDateTime

class CampaignParticipationUser(
    val seq: Long? = null,
    val campaignParticipationUserId: EntityId,
    val campaignId: EntityId,
    val userId: EntityId,
    var participationCount: Int,
    var lastParticipationDateTime: ZonedDateTime? = null,
) {
    companion object {
        fun create(userId: EntityId, campaignId: EntityId, now: ZonedDateTime) = CampaignParticipationUser(
            campaignParticipationUserId = EntityId(),
            campaignId = campaignId,
            userId = userId,
            participationCount = 1,
            lastParticipationDateTime = now
        )
    }

    fun update(now: ZonedDateTime): CampaignParticipationUser {
        this.participationCount += 1
        this.lastParticipationDateTime = now
        return this
    }
}

