package com.statsbot.member.domain.model

data class Member(

    val id: Long,

    val username: String,

    val weekTime: Long,

    val totalTime: Long,

    val loveTime: Long,

)
