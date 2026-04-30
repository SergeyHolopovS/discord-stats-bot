package com.statsbot.member.application.usecase.updateVoiceInfo

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class UpdateVoiceInfoUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: UpdateVoiceInfoCommand) {
        // Если пользователя нет - создаём и задаём информацию
        if (!repository.existsById(command.userId)) {
            repository.save(
                userId = command.userId,
                username = command.username,
                joinedAt = command.joinedAt,
                currentVoice = command.channelId
            )
            return
        }

        // Иначе просто руками задаём информацию о войсе
        repository.setJoinedAt(command.userId, command.joinedAt)
        repository.setCurrentChannel(command.userId, command.channelId)
    }

}