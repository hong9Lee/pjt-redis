package com.task.pjtredis.controller

import com.task.pjtredis.domain.EntityId
import com.task.pjtredis.service.ParticipateAdCampaignService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/campaigns")
class CampaignParticipationController(
    private val participateService: ParticipateAdCampaignService
) {

    @PostMapping("/{campaignId}/participate")
    fun participate(
        @PathVariable campaignId: String,
        @RequestBody userId: String
    ): ResponseEntity<String> {
        participateService.participateStampede(EntityId(userId), EntityId(campaignId))
        return ResponseEntity.ok("참여 성공")
    }
}
