package com.statsbot.member.application.usecase.clearWeek

import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class ClearWeekUseCase (
    private val repository: MemberRepository
) {

    fun execute()
        = repository.clearWeekTime()

}