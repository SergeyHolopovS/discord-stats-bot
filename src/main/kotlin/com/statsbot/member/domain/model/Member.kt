package com.statsbot.member.domain.model

import java.time.Instant

data class Member(

    val id: Long,

    val username: String,

    val weekTime: Long,

    val totalTime: Long,

    val loveTime: Long,

    val joinedVoiceAt: Instant? = null,

    val currentVoice: String? = null,

)
