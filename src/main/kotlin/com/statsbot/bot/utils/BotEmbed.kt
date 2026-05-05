package com.statsbot.bot.utils

import net.dv8tion.jda.api.EmbedBuilder
import net.dv8tion.jda.api.entities.MessageEmbed
import java.time.Instant

class BotEmbed(
    private val botState: BotState,
) {

    fun build(
        title: String,
        description: String? = null,
        thumbnailUrl: String? = null,
        fields: List<MessageEmbed.Field>? = null
    ): MessageEmbed {
        val embed = EmbedBuilder()
            .setColor(botState.embedColor)
            .setTitle(title)
            .setDescription(description)
            .setThumbnail(thumbnailUrl)
            .setFooter(Instant.now().toString())

        if (fields != null)
            for (field in fields)
                embed.addField(field)

        return embed.build()
    }

}