package com.statsbot.exceptions.member

import com.statsbot.exceptions.basic.BasicException

data class MemberNotFoundException(
    override val message: String = "Пользователь не найден"
) : BasicException(message)
