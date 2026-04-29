package com.statsbot.listener.commands

import com.statsbot.member.application.usecase.addLove.AddLoveTimeCommand
import com.statsbot.member.application.usecase.addLove.AddLoveTimeUseCase
import com.statsbot.member.application.usecase.addTotal.AddTotalTimeCommand
import com.statsbot.member.application.usecase.addTotal.AddTotalTimeUseCase
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class AddTimeListener (
    override val command: CommandData = Commands
        .slash("addtime", "Добавить общее время в войсе")
        .addOption(OptionType.STRING, "type", "Тип", false)
        .addOption(OptionType.USER, "user", "Пользователь", false)
        .addOption(OptionType.NUMBER, "hours", "Количество часов", false)
        .addOption(OptionType.NUMBER, "minutes", "Количество минут", false)
        .addOption(OptionType.NUMBER, "seconds", "Количество секунд", false)
        .setDefaultPermissions(
            DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
        ),
    private val addTotalTimeUseCase: AddTotalTimeUseCase,
    private val addLoveTimeUseCase: AddLoveTimeUseCase
) : Command() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return

        val user = event.getOption("user")?.asUser ?: event.user
        val hours = event.getOption("hours")?.asDouble?.toLong() ?: 0L
        val minutes = event.getOption("minutes")?.asDouble?.toLong() ?: 0L
        val seconds = event.getOption("seconds")?.asDouble?.toLong() ?: 0L
        val type = event.getOption("type")?.asString

        when (type) {
            "love" ->
                addLoveTimeUseCase.execute(
                    AddLoveTimeCommand(
                        userId = user.id,
                        username = user.name,
                        time = hours * 3600 + minutes * 60 + seconds,
                    )
                )
            else ->
                addTotalTimeUseCase.execute(
                AddTotalTimeCommand(
                    userId = user.id,
                    username = user.name,
                    time = hours * 3600 + minutes * 60 + seconds,
                )
            )
        }


        val embed = EmbedBuilder()
            .setColor(5793266)
            .setTitle("**Пользователю ${user.name} добавлено:**")
            .addField(
                "> **Время ${if (type == "love") "лав румы" else "войсов"}**",
                "```$hours ч. $minutes мин. $seconds сек.```",
                true
            )
            .setThumbnail(event.user.avatarUrl)
            .build()

        event.replyEmbeds(embed)
            .queue()
    }

}