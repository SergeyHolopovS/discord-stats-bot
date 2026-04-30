package com.statsbot.member.application.usecase.handleJoin

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class HandleJoinUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: HandleJoinCommand) {
        // Если пользователя нет создаём сразу с информацией о канале и возвращаем
        if (!repository.existsById(command.userId)) {
            repository.save(
                userId = command.userId,
                username =  command.username,
                currentVoice =  command.channelId,
                joinedAt = Instant.now(),
            )
            return
        }

        // Задаём id канала и время входа
        repository.setCurrentChannel(command.userId, command.channelId)
        repository.setJoinedAt(command.userId, Instant.now())
    }

}