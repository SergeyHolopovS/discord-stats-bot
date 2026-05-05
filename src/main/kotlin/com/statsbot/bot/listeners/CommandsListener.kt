package com.statsbot.bot.listeners

import com.statsbot.exceptions.basic.BasicException
import com.statsbot.exceptions.command.CommandNotFoundException
import com.statsbot.exceptions.member.MemberNotFoundException
import com.statsbot.bot.commands.Command
import com.statsbot.bot.utils.BotEmbed
import com.statsbot.bot.utils.BotState
import com.statsbot.member.application.usecase.create.CreateMemberCommand
import com.statsbot.member.application.usecase.create.CreateMemberUseCase
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class CommandsListener(
    @Value($$"${app.admin-role-id}")
    private val adminRoleId: String,
    private val commands: List<Command>,
    private val createMember: CreateMemberUseCase,
    private val botState: BotState,
    private val botEmbed: BotEmbed
): ListenerAdapter() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent)
        = interaction(event)

    private fun interaction(event: SlashCommandInteractionEvent, recursive: Boolean = false) {
        if (!botState.isEnabled && event.member?.roles?.map { it.id }?.contains(adminRoleId) != true)
            event.replyEmbeds(
                botEmbed.build(
                    title = "**Технические работы**",
                    description = "Бот находится на технических работах, попробуйте позже",
                    thumbnailUrl = event.user.avatarUrl
                )
            ).queue()
        try {
            val currentCommand: Command? = commands.find { command -> command.command.name == event.name }
            if (currentCommand == null) throw CommandNotFoundException()
            currentCommand.execute(event)
        } catch (exception: MemberNotFoundException) {
            if (recursive) throw exception
            val target = event.getOption("user")?.asMember
            createMember.execute(
                if (target == null)
                    CreateMemberCommand(
                        id = event.user.id,
                        username = event.user.name,
                    )
                else
                    CreateMemberCommand(
                        id = target.id,
                        username = target.user.name,
                    )
            )
            interaction(
                event = event,
                recursive = true
            )
        } catch (exception: BasicException) {
            event.replyEmbeds(
                botEmbed.build(
                    title = "**Ошибка**",
                    description = exception.message,
                    thumbnailUrl = event.user.avatarUrl
                )
            ).setEphemeral(exception.ephemeral).queue()
        } catch (exception: Exception) {
            event.replyEmbeds(
                botEmbed.build(
                    title = "**Ошибка**",
                    description = "Обратитесь к администратору бота",
                    thumbnailUrl = event.user.avatarUrl
                )
            ).queue()
            exception.printStackTrace()
        }
    }

}