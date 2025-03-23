package com.task.pjtredis.repository

import com.task.pjtredis.entity.CampaignEntity
import com.task.pjtredis.domain.EntityId
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository

interface CampaignRepository : CrudRepository<CampaignEntity, Long> {

    @Query("SELECT EXISTS (SELECT 1 FROM CampaignEntity a WHERE a.name = :name)")
    fun existsByName(name: String): Boolean

    fun findByCampaignId(campaignId: EntityId): CampaignEntity?
}
