package com.statsbot.member.application.usecase.getTotal

data class GetTotalTimeCommand(
    val userId: String,
    val username: String,
    val weekly: Boolean = false
)
