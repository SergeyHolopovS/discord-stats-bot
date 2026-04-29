package com.statsbot.listener

import com.statsbot.service.VoiceStatsService
import net.dv8tion.jda.api.events.guild.voice.GuildVoiceUpdateEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.stereotype.Component

@Component
class VoiceListener(private val service: VoiceStatsService) : ListenerAdapter() {

    override fun onGuildVoiceUpdate(event: GuildVoiceUpdateEvent) {
        val userId = event.entity.id
        val username = event.entity.user.name

        if (event.channelJoined != null) {
            service.userJoined(userId)
        }

        if (event.channelLeft != null) {
            service.userLeft(userId, username, event.channelLeft!!.id)
        }
    }
}