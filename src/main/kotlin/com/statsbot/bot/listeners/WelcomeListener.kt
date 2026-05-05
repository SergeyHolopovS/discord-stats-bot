package com.statsbot.bot.listeners

import com.statsbot.exceptions.basic.BasicException
import com.statsbot.bot.utils.BotEmbed
import com.statsbot.member.application.usecase.create.CreateMemberCommand
import com.statsbot.member.application.usecase.create.CreateMemberUseCase
import mu.KotlinLogging
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component


@Component
class WelcomeListener(
    @Value($$"${app.welcome-log-channel}")
    private val channelId: String,
    private val createMemberUseCase: CreateMemberUseCase,
    private val botEmbed: BotEmbed
): ListenerAdapter() {

    private val logger = KotlinLogging.logger {}

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        logger.info("Member ${event.user.name} joined the server")

        val channel: TextChannel? = event.jda.getTextChannelById(channelId)

        if (channel != null) {
            try {
                createMemberUseCase.execute(
                    CreateMemberCommand(
                        id = event.user.id,
                        username = event.user.name,
                    )
                )
            } catch (e: BasicException) {
                channel.sendMessageEmbeds(
                    botEmbed.build(
                        title = "**Ошибка занесения в базу данных ${event.user.name}**",
                        description = e.message,
                        thumbnailUrl = event.user.avatarUrl,
                    )
                ).queue()
            }
            channel.sendMessageEmbeds(
                botEmbed.build(
                    title = "**Вход игрока**",
                    description = "Игрок **${event.user.name}** зашёл на сервер",
                    thumbnailUrl = event.user.avatarUrl,
                )
            ).queue()
        } else logger.warn("Не найден канал логов о входе пользователя")
    }

}