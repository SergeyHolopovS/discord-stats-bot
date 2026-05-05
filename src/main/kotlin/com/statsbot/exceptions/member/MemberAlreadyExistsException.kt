package com.statsbot.exceptions.member

import com.statsbot.exceptions.basic.BasicException

data class MemberAlreadyExistsException(
    override val message: String = "Пользователь уже существует",
) : BasicException(message)
