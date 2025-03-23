package com.task.pjtredis.repository

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.entity.CampaignConditionEntity
import org.springframework.data.repository.CrudRepository

interface CampaignConditionRepository : CrudRepository<CampaignConditionEntity, Long> {
    fun findByCampaignId(campaignId: EntityId): CampaignConditionEntity?
}
