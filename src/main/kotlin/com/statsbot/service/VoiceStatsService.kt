package com.statsbot.service

import com.statsbot.member.application.usecase.addLove.AddLoveTimeCommand
import com.statsbot.member.application.usecase.addLove.AddLoveTimeUseCase
import com.statsbot.member.application.usecase.addTotal.AddTotalTimeCommand
import com.statsbot.member.application.usecase.addTotal.AddTotalTimeUseCase
import com.statsbot.member.application.usecase.getLove.GetLoveTimeCommand
import com.statsbot.member.application.usecase.getLove.GetLoveTimeUseCase
import com.statsbot.member.application.usecase.getTotal.GetTotalTimeCommand
import com.statsbot.member.application.usecase.getTotal.GetTotalTimeUseCase
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class VoiceStatsService(
    @Value($$"${app.love-channel-id}")
    private val loveChannelId: String,
    private val addTotalTimeUseCase: AddTotalTimeUseCase,
    private val addLoveTimeUseCase: AddLoveTimeUseCase,
) {

    private val joinTimes: MutableMap<String?, Instant?> = HashMap()

    fun userJoined(userId: String) {
        joinTimes[userId] = Instant.now()
    }

    fun userLeft(userId: String, username: String, channelId: String) {
        val join = joinTimes.remove(userId) ?: return

        val seconds = Instant.now().epochSecond - join.epochSecond

        if (channelId == loveChannelId) addLoveTimeUseCase.execute(
            AddLoveTimeCommand(
                userId = userId,
                username = username,
                time = seconds,
            )
        )
        else addTotalTimeUseCase.execute(
            AddTotalTimeCommand(
                userId = userId,
                username = username,
                time = seconds,
            )
        )
    }

}