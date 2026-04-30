package com.statsbot.member.domain.repository

import com.statsbot.member.domain.model.Member
import java.time.Instant

interface MemberRepository {

    fun save(
        userId: String,
        username: String,
        currentVoice: String? = null,
        joinedAt: Instant? = null,
    ): Member

    fun findById(userId: String): Member

    fun getTop10(weekly: Boolean = false): List<Member>

    fun getTotalTime(id: String, username: String, weekly: Boolean = false): Long

    fun getLoveTime(id: String, username: String): Long

    fun addTotalTime(id: String, time: Long)

    fun addWeekTime(id: String, time: Long)

    fun addLoveTime(id: String, time: Long)

    fun clearWeekTime()

    fun setCurrentChannel(userId: String, channelId: String)

    fun setJoinedAt(userId: String, date: Instant)

    fun clearCurrentChannel(userId: String)

    fun clearJoinedAt(userId: String)

    fun existsById(userId: String): Boolean

}