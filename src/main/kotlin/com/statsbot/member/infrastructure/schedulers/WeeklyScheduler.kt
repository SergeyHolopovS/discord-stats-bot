package com.statsbot.member.infrastructure.schedulers

import com.statsbot.member.domain.repository.MemberRepository
import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.entities.Message.MentionType
import net.dv8tion.jda.api.entities.channel.concrete.TextChannel
import org.springframework.beans.factory.annotation.Value
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component


@Component
class WeeklyScheduler(
    @Value($$"${app.news-channel}")
    private val newsChannel: String,
    private val repository: MemberRepository,
    private val jda: JDA,
) {

    @Scheduled(cron = "0 0 0 ? * MON", zone = "Europe/Moscow")
    fun sendWeeklyTop() {

        val top = repository.getTop10(weekly = true)

        println("Недельное время очищено")
        val channel: TextChannel? = jda.getTextChannelById(newsChannel)

        val embed = EmbedBuilder()
            .setColor(5793266)
            .setTitle("**📊 Недельный топ!**")

        for (member in top) {
            embed
                .addField(
                    "> **Топ ${top.indexOf(member)+1} — ${jda.getUserById(member.id)?.name ?: member.username}**",
                    "```${member.totalTime / 3600} ч. ${(member.totalTime % 3600) / 60} мин. ${member.totalTime%60} сек.```",
                    false
                )
        }

        channel?.sendMessageEmbeds(embed.build())?.queue()
    }

}