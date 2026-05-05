package com.statsbot.bot.utils

import org.springframework.stereotype.Component
import kotlin.concurrent.Volatile

@Component
class BotState {
    @Volatile
    var isEnabled: Boolean = false
    @Volatile
    var embedColor: Int = 5793266
}