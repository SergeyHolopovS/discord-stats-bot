package com.statsbot.listener.commands

import com.statsbot.member.application.usecase.updateVoiceInfo.UpdateVoiceInfoCommand
import com.statsbot.member.application.usecase.updateVoiceInfo.UpdateVoiceInfoUseCase
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.Permission
import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.DefaultMemberPermissions
import net.dv8tion.jda.api.interactions.commands.OptionType
import net.dv8tion.jda.api.interactions.commands.build.CommandData
import net.dv8tion.jda.api.interactions.commands.build.Commands
import org.springframework.stereotype.Component
import java.time.Instant

@Component
class UpdateVoiceInfoListener(
    override val command: CommandData = Commands
        .slash("updatevoice", "Обновить информацию о текущем войсе")
        .addOption(OptionType.CHANNEL, "channel", "Текущий канал пользователя", true)
        .addOption(OptionType.USER, "user", "Пользователь", false)
        .addOption(OptionType.NUMBER, "hours", "Количество часов с присоединения", false)
        .addOption(OptionType.NUMBER, "minutes", "Количество минут с присоединения", false)
        .addOption(OptionType.NUMBER, "seconds", "Количество секунд с присоединения", false)
        .setDefaultPermissions(
            DefaultMemberPermissions.enabledFor(Permission.ADMINISTRATOR)
        ),
    private val updateVoiceInfoUseCase: UpdateVoiceInfoUseCase
) : Command() {

    override fun onSlashCommandInteraction(event: SlashCommandInteractionEvent) {
        if (event.name != command.name) return

        val user = event.getOption("user")?.asUser ?: event.user
        val channel = event.getOption("channel")?.asChannel
        val hours = event.getOption("hours")?.asDouble?.toLong() ?: 0L
        val minutes = event.getOption("minutes")?.asDouble?.toLong() ?: 0L
        val seconds = event.getOption("seconds")?.asDouble?.toLong() ?: 0L

        updateVoiceInfoUseCase.execute(
            UpdateVoiceInfoCommand(
                userId = user.id,
                username = user.name,
                channelId = channel!!.id,
                joinedAt = Instant.now().minusSeconds(hours * 3600 + minutes * 60 + seconds),
            )
        )

        val embed = EmbedBuilder()
            .setColor(5793266)
            .setTitle("**Пользователю ${user.name} обновлена информация о голосовом канале**")
            .setThumbnail(event.user.avatarUrl)
            .build()

        event.replyEmbeds(embed)
            .queue()
    }

}