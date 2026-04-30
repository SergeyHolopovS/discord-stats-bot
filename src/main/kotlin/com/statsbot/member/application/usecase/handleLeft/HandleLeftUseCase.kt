package com.statsbot.member.application.usecase.handleLeft

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class HandleLeftUseCase(
    @Value($$"${app.love-channel-id}")
    private val loveChannelId: String,
    private val repository: MemberRepository
) {

    fun execute(command: HandleLeftCommand) {
        // Если вышел пользователь, которого нет в базе данных - создаём и ничего не делаем
        if (!repository.existsById(command.userId)) {
            repository.save(command.userId, command.username)
            return
        }

        // Иначе получаем пользователя (доверяем без try/catch, так как была проверка наличия)
        val member = repository.findById(command.userId)
        // Сверяем id каналов
        if (member.currentVoice == command.channelId && member.joinedVoiceAt != null) {
            // Если всё хорошо - считаем отсиженные секунды
            val seconds = Duration.between(member.joinedVoiceAt, Instant.now()).seconds

            // Далее добавляем в общее время и недельное время
            repository.addTotalTime(command.userId, seconds)
            repository.addWeekTime(command.userId, seconds)

            // Также если это лав рума - добавляем в статистику лав румы
            if (command.channelId == loveChannelId)
                repository.addLoveTime(command.userId, seconds)

            // Очищаем информацию о каналах
            repository.clearJoinedAt(command.userId)
            repository.clearCurrentChannel(command.userId)
        }
        // Иначе пишем лог о подозрительной активности
        else println(
            "ID каналов не совпадают у ${command.username}\n" +
            "Real channel ID: ${command.channelId}\n" +
            "DB channel ID: ${member.currentVoice}\n" +
            "Joined voice at: ${member.joinedVoiceAt}"
        )
    }

}