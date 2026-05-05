package com.statsbot.member.application.usecase.create

data class CreateMemberCommand (
    val id: String,
    val username: String,
)