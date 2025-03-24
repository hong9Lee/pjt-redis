package com.task.pjtredis.repository

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.entity.CampaignParticipationEntity
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CampaignParticipationRepository : CrudRepository<CampaignParticipationEntity, Long> {
    fun findByCampaignId(campaignId: EntityId): CampaignParticipationEntity?

    @Modifying(clearAutomatically = true, flushAutomatically = true)
    @Query("UPDATE CampaignParticipationEntity a SET a.currentParticipation = a.currentParticipation + 1 WHERE a.campaignId = :campaignId")
    fun incrementParticipationCount(campaignId: EntityId): Int
}
