package com.task.pjtredis.domain.campaign

import com.task.pjtredis.domain.EntityId

class CampaignParticipation(
    val seq: Long? = 0,
    val campaignParticipationId: EntityId,
    val campaignId: EntityId,
    var maxParticipation: Int,
    var currentParticipation: Int = 0,
) {
    companion object {
        fun create(campaignId: EntityId, maxParticipation: Int): CampaignParticipation {
            return CampaignParticipation(
                campaignParticipationId = EntityId(),
                campaignId = campaignId,
                maxParticipation = maxParticipation,
                currentParticipation = 0
            )
        }
    }

    fun canParticipate(): Boolean {
        return currentParticipation < maxParticipation
    }

    fun updateCurrentParticipation(): Int {
        this.currentParticipation += 1
        return currentParticipation
    }

    fun getCurrentParticipationCount(): Int {
        return maxParticipation - currentParticipation
    }
}
