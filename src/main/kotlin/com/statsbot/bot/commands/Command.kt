package com.statsbot.bot.commands

import net.dv8tion.jda.api.events.interaction.command.SlashCommandInteractionEvent
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class Command {
    abstract val command: CommandData

    abstract fun execute(event: SlashCommandInteractionEvent)
}