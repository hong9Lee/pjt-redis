package com.task.pjtredis.repository

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.entity.CampaignParticipationUserEntity
import org.springframework.data.repository.CrudRepository

interface CampaignParticipationUserRepository : CrudRepository<CampaignParticipationUserEntity, Long> {
    fun findByUserIdAndCampaignId(userId: EntityId, campaignId: EntityId): CampaignParticipationUserEntity?
}
