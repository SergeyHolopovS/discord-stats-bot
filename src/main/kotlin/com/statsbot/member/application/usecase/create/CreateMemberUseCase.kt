package com.statsbot.member.application.usecase.create

import com.statsbot.exceptions.member.MemberAlreadyExistsException
import com.statsbot.member.domain.model.Member
import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class CreateMemberUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: CreateMemberCommand): Member {
        // Проверяем существует ли по ID
        if (repository.existsById(command.id))
            throw MemberAlreadyExistsException()

        // Сохраняем в базу данных
        return repository.save(
            userId = command.id,
            username = command.username,
        )
    }

}