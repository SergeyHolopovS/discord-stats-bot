package com.statsbot.listener.commands

import com.statsbot.member.application.usecase.getLove.GetLoveTimeCommand
import com.statsbot.member.application.usecase.getLove.GetLoveTimeUseCase
import com.statsbot.member.application.usecase.getTotal.GetTotalTimeCommand
import com.statsbot.member.application.usecase.getTotal.GetTotalTimeUseCase
import com.statsbot.service.VoiceStatsService
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class StatsCommandListener(
    override val command: CommandData = Commands
        .slash("stats", "Показать время в голосе")
        .addOption(OptionType.STRING, "type", "Тип", false)
        .addOption(OptionType.BOOLEAN, "weekly", "Недельный вариант", false),
    private val getTotalTimeUseCase: GetTotalTimeUseCase,
    private val getLoveTimeUseCase: GetLoveTimeUseCase,
) : Command() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return

        val type = event.getOption("type")?.asString
        val weekly = event.getOption("weekly")?.asBoolean ?: false

        val allSeconds = if (type == "love")
                            getLoveTimeUseCase.execute(
                                GetLoveTimeCommand(
                                    event.user.id,
                                    event.user.name
                                )
                            )
                            else getTotalTimeUseCase.execute(
                                GetTotalTimeCommand(
                                    event.user.id,
                                    event.user.name,
                                    weekly
                                )
                            )

        val hours = allSeconds / 3600
        val minutes = (allSeconds % 3600) / 60
        val seconds = allSeconds % 60

        val embed = EmbedBuilder()
            .setColor(5793266)
            .setTitle("**Статистика ${if (type == "love") "лаврумы" else "войсов"}${if (weekly) " за неделю" else ""} — ${event.user.name}**")
            .setDescription("```$hours ч. $minutes мин. $seconds сек.```")
            .setThumbnail(event.user.avatarUrl)
            .build()

        event.replyEmbeds(embed)
            .queue()
    }

}