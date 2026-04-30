package com.statsbot.member.application.usecase.getTop10

import com.statsbot.member.domain.model.Member
import com.statsbot.member.domain.repository.MemberRepository
import org.springframework.stereotype.Component
import java.time.Duration
import java.time.Instant

@Component
class GetTopTenUseCase(
    private val repository: MemberRepository
) {

    fun execute(command: GetTopTenCommand): GetTopTenResult {
        // Получаем топ и сортируем, учитывая текущее время в войсе
        val top = repository.getTop10(command.weekly).sortedByDescending { member ->
            val additionalTime =
                if (member.joinedVoiceAt != null)
                    Duration.between(member.joinedVoiceAt, Instant.now()).toSeconds()
                else 0L
            member.totalTime.plus(additionalTime)
        }

        return GetTopTenResult(
            top.map {
                // Проверяем, что joinedVoiceAt не null перед вычислением времени
                val voiceTimeInSeconds =
                    if (it.joinedVoiceAt != null)
                        Duration.between(it.joinedVoiceAt, Instant.now()).toSeconds()
                    else 0L
                GetTopTenShortMember(
                    username = it.username,
                    time = it.totalTime.plus(voiceTimeInSeconds)
                )
            }
        )
    }

}
