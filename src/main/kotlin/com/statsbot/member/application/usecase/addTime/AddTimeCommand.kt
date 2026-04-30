package com.statsbot.member.application.usecase.addTime

data class AddTimeCommand(
    val userId: String,
    val username: String,
    val time: Long,
    val love: Boolean = false,
)
