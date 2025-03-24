package com.task.pjtredis.entity

import com.task.ad.support.ListStringConverter
import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.domain.campaign.CampaignCondition
import com.task.pjtredis.entity.base.BaseTimeEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Convert
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "campaign_condition")
class CampaignConditionEntity(
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_condition_id"))
    private val campaignConditionId: EntityId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_id"))
    private val campaignId: EntityId,

    @Column(name = "first_time_only", nullable = false)
    private val firstTimeOnly: Boolean,

    @Column(name = "min_participation", nullable = false)
    private val minParticipation: Int,

    @Column(name = "required_previous_ad", nullable = false)
    private val requiredPreviousAd: Boolean,

    @Column(name = "required_previous_ads")
    @Convert(converter = ListStringConverter::class)
    private val requiredPreviousAds: List<String>
) : BaseTimeEntity() {

    fun toDomain(): CampaignCondition {
        return CampaignCondition(
            campaignConditionId = this.campaignConditionId,
            campaignId = this.campaignId,
            firstTimeOnly = this.firstTimeOnly,
            minParticipation = this.minParticipation,
            requiredPreviousAd = this.requiredPreviousAd,
            requiredPreviousAds = this.requiredPreviousAds.map { EntityId(it) }
        )
    }

    companion object {
        fun of(campaignCondition: CampaignCondition): CampaignConditionEntity {
            return CampaignConditionEntity(
                campaignConditionId = campaignCondition.campaignConditionId,
                campaignId = campaignCondition.campaignId,
                firstTimeOnly = campaignCondition.firstTimeOnly,
                minParticipation = campaignCondition.minParticipation,
                requiredPreviousAd = campaignCondition.requiredPreviousAd,
                requiredPreviousAds = campaignCondition.requiredPreviousAds.map { it.value }
            )
        }
    }
}
