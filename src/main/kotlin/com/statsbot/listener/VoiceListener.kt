package com.statsbot.listener

import com.statsbot.member.application.usecase.handleJoin.HandleJoinCommand
import com.statsbot.member.application.usecase.handleJoin.HandleJoinUseCase
import com.statsbot.member.application.usecase.handleLeft.HandleLeftCommand
import com.statsbot.member.application.usecase.handleLeft.HandleLeftUseCase
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class VoiceListener(
    private val handleJoin: HandleJoinUseCase,
    private val handleLeft: HandleLeftUseCase,
) : ListenerAdapter() {

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        val userId = event.entity.id
        val username = event.entity.user.name

        if (event.channelJoined != null)
            handleJoin.execute(
                HandleJoinCommand(userId, username, event.channelJoined!!.id)
            )

        if (event.channelLeft != null)
            handleLeft.execute(
                HandleLeftCommand(userId, username, event.channelLeft!!.id)
            )
    }
}