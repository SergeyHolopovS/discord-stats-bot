package com.statsbot.member.application.usecase.addTotal

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class AddTotalTimeUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: AddTotalTimeCommand)
        = repository.allTotalTime(command.userId, command.username, command.time)

}