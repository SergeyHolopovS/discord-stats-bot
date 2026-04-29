package com.statsbot.member.application.usecase.getLove

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class GetLoveTimeUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: GetLoveTimeCommand): Long
        = repository.getLoveTime(command.userId, command.username)

}