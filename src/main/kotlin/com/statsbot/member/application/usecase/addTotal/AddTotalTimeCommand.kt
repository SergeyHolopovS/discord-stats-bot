package com.statsbot.member.application.usecase.addTotal

data class AddTotalTimeCommand(
    val userId: String,
    val username: String,
    val time: Long,
)
