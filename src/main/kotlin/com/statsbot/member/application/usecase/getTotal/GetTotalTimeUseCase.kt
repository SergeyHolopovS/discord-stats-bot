package com.statsbot.member.application.usecase.getTotal

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class GetTotalTimeUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: GetTotalTimeCommand): Long
        = repository.getTotalTime(command.userId, command.username, command.weekly)

}