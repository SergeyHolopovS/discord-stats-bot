package com.statsbot.member.application.usecase.updateVoiceInfo

import java.time.Instant

data class UpdateVoiceInfoCommand(
    val userId: String,
    val username: String,
    val channelId: String,
    val joinedAt: Instant = Instant.now(),
)
