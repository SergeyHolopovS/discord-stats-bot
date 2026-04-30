package com.statsbot.member.application.usecase.getTime

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class GetTimeUseCase(
    @Value($$"${app.love-channel-id}")
    private val loveChannelId: String,
    private val repository: MemberRepository
) {

    fun execute(command: GetTimeCommand): GetTimeResult {
        // Если пользователя не существует - создаём пустого и возвращаем все 0
        if (!repository.existsById(command.userId)) {
            repository.save(
                userId = command.userId,
                username = command.username,
            )
            return GetTimeResult(
                total = 0L,
                week = 0L,
                love = 0L,
            )
        }

        // Иначе получаем пользователя из бд
        val member = repository.findById(command.userId)

        // Считаем сколько просидели сейчас
        val seconds: Long =
            if (member.currentVoice != null && member.joinedVoiceAt != null)
                Duration.between(member.joinedVoiceAt, Instant.now()).seconds
            else 0L

        // Возвращаем статистику
        return GetTimeResult(
            total = member.totalTime + seconds,
            week = member.weekTime + seconds,
            love =
                if (member.currentVoice == loveChannelId)
                    member.loveTime + seconds
                else member.loveTime,
        )
    }

}