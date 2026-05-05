package com.statsbot.bot.commands

import com.statsbot.bot.utils.BotEmbed
import com.statsbot.member.application.usecase.getTime.GetTimeCommand
import com.statsbot.member.application.usecase.getTime.GetTimeUseCase
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class StatsCommand(
    override val command: CommandData = Commands
        .slash("stats", "Показать время в голосе")
        .addOption(OptionType.USER, "user", "Пользователь", false),
    private val getTimeUseCase: GetTimeUseCase,
    private val botEmbed: BotEmbed,
) : Command() {

    override fun execute(event: SlashCommandInteractionEvent) {
        val user = event.getOption("user")?.asUser ?: event.user

        val stats = getTimeUseCase.execute(
            GetTimeCommand(
                userId = user.id,
                username = user.name,
            )
        )

        val fields = mutableListOf(
            // Поле общего времени
            MessageEmbed.Field(
                "> **Общее время**",
                parseTime(stats.total),
                false
            ),
            // Поле недельного времени
            MessageEmbed.Field(
                "> **Недельное время**",
                parseTime(stats.week),
                false
            )
        )

        // Поле времени в лав руме
        if (stats.love > 300) // Только если это время > 5 минут
            fields.add(
                MessageEmbed.Field(
                    "> **Время в лав руме**",
                    parseTime(stats.love),
                    false
                )
            )

        event.replyEmbeds(
            botEmbed.build(
                title = "**Статистика войсов — ${user.name}**",
                fields = fields,
                thumbnailUrl = user.avatarUrl,
            )
        ).queue()
    }

    private fun parseTime(time: Long): String
        = "```${time / 3600} ч. ${(time % 3600) / 60} мин. ${time % 60} сек.```"

}