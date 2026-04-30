package com.statsbot.member.application.usecase.handleLeft

data class HandleLeftCommand(
    val userId: String,
    val username: String,
    val channelId: String,
)
