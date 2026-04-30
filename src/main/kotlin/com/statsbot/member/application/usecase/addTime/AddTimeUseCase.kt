package com.statsbot.member.application.usecase.addTime

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class AddTimeUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: AddTimeCommand) {
        // Если команда обращена к пользователю, которого нет в базе данных - создаём
        if (!repository.existsById(command.userId))
            repository.save(command.userId, command.username)

        // Инкрементируем общее и недельное время
        repository.addTotalTime(command.userId, command.time)
        repository.addWeekTime(command.userId, command.time)

        // Если love = true - добавляем ещё и время в лав руме
        if (command.love)
            repository.addLoveTime(command.userId, command.time)
    }

}