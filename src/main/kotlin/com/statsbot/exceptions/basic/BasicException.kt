package com.statsbot.exceptions.basic

open class BasicException(
    override val message: String,
    open val ephemeral: Boolean = false
): RuntimeException(message)