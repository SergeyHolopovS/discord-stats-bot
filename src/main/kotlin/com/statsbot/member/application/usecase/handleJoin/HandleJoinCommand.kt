package com.statsbot.member.application.usecase.handleJoin

data class HandleJoinCommand(
    val userId: String,
    val username: String,
    val channelId: String,
)
