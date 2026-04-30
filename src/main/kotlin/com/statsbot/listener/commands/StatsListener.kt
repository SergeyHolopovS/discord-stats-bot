package com.statsbot.listener.commands

import com.statsbot.member.application.usecase.getTime.GetTimeCommand
import com.statsbot.member.application.usecase.getTime.GetTimeUseCase
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class StatsListener(
    override val command: CommandData = Commands
        .slash("stats", "Показать время в голосе")
        .addOption(OptionType.USER, "user", "Пользователь", false),
    private val getTimeUseCase: GetTimeUseCase,
) : Command() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return

        val user = event.getOption("user")?.asUser ?: event.user

        val embed = EmbedBuilder()
            .setColor(5793266)
            .setTitle("**Статистика войсов — ${user.name}**")
            .setThumbnail(user.avatarUrl)

        val stats = getTimeUseCase.execute(
            GetTimeCommand(
                userId = user.id,
                username = user.name,
            )
        )

        // Поле общего времени
        embed.addField(
            "> **Общее время**",
            parseTime(stats.total),
            false
        )

        // Поле недельного времени
        embed.addField(
            "> **Недельное время**",
            parseTime(stats.week),
            false
        )

        // Поле времени в лав руме
        if (stats.love > 300) // Только если это время > 5 минут
            embed.addField(
                "> **Время в лав руме**",
                parseTime(stats.love),
                false
            )

        event.replyEmbeds(embed.build())
            .queue()
    }

    private fun parseTime(time: Long): String
        = "```${time / 3600} ч. ${(time % 3600) / 60} мин. ${time % 60} сек.```"

}