package com.statsbot.listener.commands

import net.dv8tion.jda.api.hooks.ListenerAdapter
import net.dv8tion.jda.api.interactions.commands.build.CommandData

abstract class Command : ListenerAdapter() {
    abstract val command: CommandData
}