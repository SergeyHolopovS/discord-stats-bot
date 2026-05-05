package com.statsbot.exceptions.channels

import com.statsbot.exceptions.basic.BasicException

data class ChannelNotFoundException(
    override val message: String = "Канал не найден",
    override val ephemeral: Boolean = true
) : BasicException(message)
