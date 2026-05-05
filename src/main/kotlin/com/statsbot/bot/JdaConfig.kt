package com.statsbot.bot

import com.statsbot.bot.listeners.VoiceListener
import com.statsbot.bot.commands.Command
import net.dv8tion.jda.api.JDA
import net.dv8tion.jda.api.JDABuilder
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import javax.security.auth.login.LoginException

@Configuration
class JdaConfig {
    @Bean
    @Throws(LoginException::class)
    fun jda(
        @Value($$"${app.token}")
        token: String,
        voiceListener: VoiceListener,
        slashCommands: List<Command>
    ): JDA {
        val jda = JDABuilder.createDefault(token)
            .addEventListeners(voiceListener)
            .addEventListeners(*slashCommands.toTypedArray())
            .build()

        // регистрация slash-команд
        jda.updateCommands()
            .addCommands(slashCommands.map { el -> el.command })
            .queue()

        return jda
    }
}