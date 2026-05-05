package com.statsbot.bot.commands

import com.statsbot.bot.utils.BotEmbed
import com.statsbot.member.application.usecase.getTop10.GetTopTenCommand
import com.statsbot.member.application.usecase.getTop10.GetTopTenUseCase
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class GetTopTenCommand(
    override val command: CommandData = Commands
        .slash("top", "Показать топ участников")
        .addOption(OptionType.BOOLEAN, "weekly", "Недельный вариант", false),
    private val getTopTenUseCase: GetTopTenUseCase,
    private val botEmbed: BotEmbed
) : Command() {


    override fun execute(event: SlashCommandInteractionEvent) {
        val top = getTopTenUseCase.execute(GetTopTenCommand()).top

        event
            .replyEmbeds(
                botEmbed.build(
                    title = "**${if (event.getOption("weekly")?.asBoolean ?: false) "Недельный топ" else "Топ ${top.size}"} пользователей**",
                    fields = top.map {
                        MessageEmbed.Field(
                            "> **Топ ${top.indexOf(it)+1} — ${it.username}**",
                            "```${it.time / 3600} ч. ${(it.time % 3600) / 60} мин. ${it.time % 60} сек.```",
                            false
                        )
                    },
                    thumbnailUrl = event.user.avatarUrl,
                )
            )
            .setAllowedMentions(listOf(MentionType.USER))
            .queue()
    }

}