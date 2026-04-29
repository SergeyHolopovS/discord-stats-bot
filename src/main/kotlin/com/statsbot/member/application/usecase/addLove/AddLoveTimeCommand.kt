package com.statsbot.member.application.usecase.addLove

data class AddLoveTimeCommand(
    val userId: String,
    val username: String,
    val time: Long,
)
