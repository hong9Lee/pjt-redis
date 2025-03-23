package com.task.pjtredis.entity

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.domain.campaign.Campaign
import com.task.pjtredis.entity.base.BaseTimeEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "campaign")
class CampaignEntity(

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_id"))
    private val campaignId: EntityId,

    @Column(name = "name")
    private val name: String,

    @Column(name = "max_count")
    private val maxCount: Int,

    @Column(name = "start_date_time")
    private val startDateTime: ZonedDateTime,

    @Column(name = "end_date_time")
    private val endDateTime: ZonedDateTime,
) : BaseTimeEntity() {

    fun toDomain(): Campaign {
        return Campaign(
            campaignId = this.campaignId,
            name = this.name,
            maxCount = this.maxCount,
            startDateTime = this.startDateTime,
            endDateTime = this.endDateTime
        )
    }

    companion object {
        fun of(campaign: Campaign): CampaignEntity {
            return CampaignEntity(
                campaignId = campaign.campaignId,
                name = campaign.name,
                maxCount = campaign.maxCount,
                startDateTime = campaign.startDateTime,
                endDateTime = campaign.endDateTime,
            )
        }
    }
}
