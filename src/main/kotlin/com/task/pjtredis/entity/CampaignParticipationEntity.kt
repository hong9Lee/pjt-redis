package com.task.pjtredis.entity

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.domain.campaign.CampaignParticipation
import com.task.pjtredis.entity.base.BaseTimeEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "campaign_participation")
class CampaignParticipationEntity(
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_participation_id"))
    private val campaignParticipationId: EntityId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_id"))
    private val campaignId: EntityId,

    @Column(name = "max_participation", nullable = false)
    private val maxParticipation: Int,

    @Column(name = "current_participation", nullable = false)
    private var currentParticipation: Int,
) : BaseTimeEntity() {

    fun toDomain(): CampaignParticipation {
        return CampaignParticipation(
            seq = this.seq,
            campaignParticipationId = this.campaignParticipationId,
            campaignId = this.campaignId,
            maxParticipation = this.maxParticipation,
            currentParticipation = this.currentParticipation
        )
    }

    companion object {
        fun of(campaignParticipation: CampaignParticipation): CampaignParticipationEntity {
            return CampaignParticipationEntity(
                campaignParticipationId = campaignParticipation.campaignParticipationId,
                campaignId = campaignParticipation.campaignId,
                maxParticipation = campaignParticipation.maxParticipation,
                currentParticipation = campaignParticipation.currentParticipation
            ).apply {
                seq = campaignParticipation.seq
            }
        }
    }
}
