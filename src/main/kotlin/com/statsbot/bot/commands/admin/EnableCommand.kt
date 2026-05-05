package com.statsbot.bot.commands.admin

import com.statsbot.bot.commands.Command
import com.statsbot.bot.utils.BotEmbed
import com.statsbot.bot.utils.BotState
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component

@Component
class EnableCommand(
    override val command: CommandData = Commands
        .slash("enable", "Вкл/Выкл технические работы")
        .addOption(OptionType.BOOLEAN, "ephemeral", "Эфемерность ответа")
        .setDefaultPermissions(
            DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
        ),
    private val botEmbed: BotEmbed,
    private val botState: BotState
) : Command() {

    override fun execute(event: SlashCommandInteractionEvent) {
        val ephemeral = event.getOption("ephemeral")?.asBoolean ?: true

        botState.isEnabled = !botState.isEnabled
        event.replyEmbeds(
            botEmbed.build(
                title = "**Технические работы**",
                description = "Технические работы были успешно **${if (botState.isEnabled) "Включены" else "Выключены"}**",
                thumbnailUrl = event.user.avatarUrl,
            )
        ).setEphemeral(ephemeral).queue()
    }

}