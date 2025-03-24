package com.task.pjtredis.entity

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.domain.campaign.CampaignParticipationUser
import com.task.pjtredis.entity.base.BaseTimeEntity
import jakarta.persistence.AttributeOverride
import jakarta.persistence.Column
import jakarta.persistence.Embedded
import jakarta.persistence.Entity
import jakarta.persistence.Table
import java.time.ZonedDateTime

@Entity
@Table(name = "campaign_participation_user")
class CampaignParticipationUserEntity(
    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_participation_user_id"))
    val campaignParticipationUserId: EntityId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "campaign_id"))
    val campaignId: EntityId,

    @Embedded
    @AttributeOverride(name = "value", column = Column(name = "user_id"))
    val userId: EntityId,

    @Column(name = "participation_count")
    var participationCount: Int,

    @Column(name = "last_participation_date_time")
    var lastParticipationDateTime: ZonedDateTime?,
) : BaseTimeEntity() {

    companion object {
        fun of(campaignParticipationUser: CampaignParticipationUser): CampaignParticipationUserEntity {
            return CampaignParticipationUserEntity(
                campaignParticipationUserId = campaignParticipationUser.campaignParticipationUserId,
                campaignId = campaignParticipationUser.campaignId,
                userId = campaignParticipationUser.userId,
                participationCount = campaignParticipationUser.participationCount,
                lastParticipationDateTime = campaignParticipationUser.lastParticipationDateTime
            ).apply {
                this.seq = campaignParticipationUser.seq
            }
        }
    }

    fun toDomain(): CampaignParticipationUser {
        return CampaignParticipationUser(
            seq = seq,
            campaignParticipationUserId = campaignParticipationUserId,
            campaignId = campaignId,
            userId = userId,
            participationCount = participationCount,
            lastParticipationDateTime = lastParticipationDateTime
        )
    }
}
