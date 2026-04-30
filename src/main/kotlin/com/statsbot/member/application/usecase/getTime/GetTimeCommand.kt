package com.statsbot.member.application.usecase.getTime

data class GetTimeCommand(
    val userId: String,
    val username: String,
)
