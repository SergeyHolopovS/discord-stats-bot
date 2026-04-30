package com.statsbot.listener.commands

import com.statsbot.member.application.usecase.getTop10.GetTopTenCommand
import com.statsbot.member.application.usecase.getTop10.GetTopTenUseCase
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class GetTopTenListener(
    override val command: CommandData = Commands
        .slash("top", "Показать топ участников")
        .addOption(OptionType.BOOLEAN, "weekly", "Недельный вариант", false),
    private val getTopTenUseCase: GetTopTenUseCase
) : Command() {


    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return

        val top = getTopTenUseCase.execute(GetTopTenCommand()).top

        val embed = EmbedBuilder()
            .setColor(5793266)
            .setTitle("**${if (event.getOption("weekly")?.asBoolean ?: false) "Недельный топ" else "Топ ${top.size}"} пользователей**")
            .setThumbnail(event.user.avatarUrl)

        for (member in top) {
            embed
                .addField(
                    "> **Топ ${top.indexOf(member)+1} — ${member.username}**",
                    "```${member.time / 3600} ч. ${(member.time % 3600) / 60} мин. ${member.time % 60} сек.```",
                    false
                )
        }

        event
            .replyEmbeds(embed.build())
            .setAllowedMentions(listOf(MentionType.USER))
            .queue()
    }

}