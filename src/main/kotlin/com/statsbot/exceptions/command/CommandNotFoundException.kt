package com.statsbot.exceptions.command

import com.statsbot.exceptions.basic.BasicException

data class CommandNotFoundException(
    override val message: String = "Команда не найдена"
) : BasicException(message)
