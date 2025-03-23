package com.task.pjtredis.domain.campaign

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.task.pjtredis.domain.EntityId

@JsonIgnoreProperties(ignoreUnknown = true)
class CampaignCondition(
    val campaignConditionId: EntityId,
    val campaignId: EntityId,
    val firstTimeOnly: Boolean = false,  // 첫 참여 유저만 가능
    val minParticipation: Int = 0,  // 최소 N번 이상 참여해야 가능
    val requiredPreviousAd: Boolean = false, // 특정 광고를 먼저 참여해야 가능
    val requiredPreviousAds: List<EntityId> = emptyList(),
    // ... 제한사항은 계속 추가될 수 있음.
) {
    companion object {
        fun create(
            campaignId: EntityId,
            firstTimeOnly: Boolean,
            minParticipation: Int,
            requiredPreviousAd: Boolean,
            requiredPreviousAds: List<EntityId>,
        ): CampaignCondition {
            return CampaignCondition(
                campaignConditionId = EntityId(),
                campaignId = campaignId,
                firstTimeOnly = firstTimeOnly,
                minParticipation = minParticipation,
                requiredPreviousAd = requiredPreviousAd,
                requiredPreviousAds = requiredPreviousAds
            )
        }
    }

    fun validate(participatedAds: Set<String>, participationCount: Int) {
        if (firstTimeOnly && campaignId.value in participatedAds) {
            throw IllegalArgumentException("해당 광고는 첫 참여자만 가능합니다.")
        }

        if (minParticipation > 0 && participationCount < minParticipation) {
            throw IllegalArgumentException("해당 광고는 최소 ${minParticipation}회 이상 참여해야 합니다.")
        }

        val requiredAdIds = requiredPreviousAds.map { it.value }.toSet()
        if (requiredAdIds.isNotEmpty() && !participatedAds.containsAll(requiredAdIds)) {
            throw IllegalArgumentException("참여하려면 먼저 ${requiredAdIds.joinToString()} 광고를 완료해야 합니다.")
        }
    }
}
