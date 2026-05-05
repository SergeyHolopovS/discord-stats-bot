package com.statsbot.bot.commands.admin

import com.statsbot.bot.commands.Command
import com.statsbot.bot.utils.BotEmbed
import com.statsbot.bot.utils.BotState
import com.statsbot.exceptions.channels.ChannelNotFoundException
import com.statsbot.exceptions.member.MemberAlreadyExistsException
import com.statsbot.member.application.usecase.create.CreateMemberCommand
import com.statsbot.member.application.usecase.create.CreateMemberUseCase
import mu.KotlinLogging
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component

@Component
class UpdateAllUsersCommand (
    override val command: CommandData = Commands
        .slash("syncdb", "Синхронизировать базу данных")
        .addOption(OptionType.BOOLEAN, "ephemeral", "Эфемерность ответа", false)
        .setDefaultPermissions(
            DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
        ),
    @Value($$"${app.bot-log-channel-id}")
    private val botLogChannelId: String,
    private val botState: BotState,
    private val botEmbed: BotEmbed,
    private val createMemberUseCase: CreateMemberUseCase
) : Command() {

    private val logger = KotlinLogging.logger {}

    override fun execute(event: SlashCommandInteractionEvent) {
        val botLogChannel = event.jda.getTextChannelById(botLogChannelId)
            ?: throw ChannelNotFoundException("Канал для логов бота не найден")
        val ephemeral = event.getOption("ephemeral")?.asBoolean ?: true

        val enabled = botState.isEnabled

        // Включаем технические работы
        botState.isEnabled = false

        botLogChannel.sendMessageEmbeds(
            botEmbed.build(
                title = "**Технические работы**",
                description = "Производится синхронизация базы данных с дискордом...",
                thumbnailUrl = event.user.avatarUrl,
            )
        ).queue()
        logger.warn("Производится синхронизация базы данных с дискордом")

        event.replyEmbeds(
            botEmbed.build(
                title = "**Выполнение**",
                description = "Производится синхронизация базы данных с дискордом, ожидайте...",
                thumbnailUrl = event.user.avatarUrl,
            )
        ).setEphemeral(ephemeral).queue()

        val members = event.guild?.members ?: listOf()

        for (member in members) {
            try {
                createMemberUseCase.execute(
                    CreateMemberCommand(
                        id = member.user.id,
                        username = member.user.name
                    )
                )
                logger.info("Пользователь ${member.user.name} был добавлен в базу данных")
                botLogChannel.sendMessageEmbeds(
                    botEmbed.build(
                        title = "**Логирование**",
                        description = "Пользователь **${member.user.name}** был добавлен в базу данных",
                        thumbnailUrl = member.user.avatarUrl,
                    )
                ).queue()
            } catch (_: MemberAlreadyExistsException) {}
        }

        // Выключаем технические работы
        botLogChannel.sendMessageEmbeds(
            botEmbed.build(
                title = "**Технические работы**",
                description = "Синхронизация базы данных **успешно завершена**"
            )
        ).queue()
        botState.isEnabled = enabled

    }

}