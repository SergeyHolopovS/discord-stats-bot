package com.statsbot.member.application.usecase.getTop10

import com.statsbot.member.domain.model.Member
import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component

@Component
class GetTopTenUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: GetTopTenCommand): List<Member>
        = repository.getTop10(command.weekly)

}
