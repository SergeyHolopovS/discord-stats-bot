package com.statsbot.member.application.usecase.addLove

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class AddLoveTimeUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: AddLoveTimeCommand)
        = repository.addLoveTime(command.userId, command.username, command.time)

}