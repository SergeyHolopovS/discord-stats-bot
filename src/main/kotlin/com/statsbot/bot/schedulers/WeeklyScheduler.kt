package com.statsbot.bot.schedulers

import com.statsbot.bot.utils.BotEmbed
import com.statsbot.member.application.usecase.clearWeek.ClearWeekUseCase
import com.statsbot.member.application.usecase.getTop10.GetTopTenCommand
import com.statsbot.member.application.usecase.getTop10.GetTopTenUseCase
import mu.KotlinLogging
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class WeeklyScheduler(
    @Value($$"${app.news-channel}")
    private val newsChannel: String,
    private val getTopTenUseCase: GetTopTenUseCase,
    private val clearWeekUseCase: ClearWeekUseCase,
    private val botEmbed: BotEmbed,
    private val jda: JDA,
) {

    private val logger = KotlinLogging.logger {}

    @Scheduled(cron = "0 0 0 ? * MON", zone = "Europe/Moscow")
    fun sendWeeklyTop() {

        val top = getTopTenUseCase.execute(
            GetTopTenCommand(
                weekly = true,
            )
        ).top
        val channel: TextChannel? = jda.getTextChannelById(newsChannel)

        if (channel == null) {
            logger.warn("Канал новостей не найден, укажите NEWS_CHANNEL в .env")
            return
        }

        channel.sendMessageEmbeds(
            botEmbed.build(
                title = "**📊 Недельный топ!**",
                fields = top.map {
                    MessageEmbed.Field(
                        "> **Топ ${top.indexOf(it)+1} — ${it.username}**",
                        "```${it.time / 3600} ч. ${(it.time % 3600) / 60} мин. ${it.time%60} сек.```",
                        false
                    )
                },
            )
        ).queue()

        clearWeekUseCase.execute()

        logger.info("Недельное время очищено")
    }

}