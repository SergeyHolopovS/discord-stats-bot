package com.statsbot.member.domain.repository

import com.statsbot.member.domain.model.Member

interface MemberRepository {

    fun getTop10(weekly: Boolean = false): List<Member>

    fun getTotalTime(id: String, username: String, weekly: Boolean = false): Long

    fun getLoveTime(id: String, username: String): Long

    fun allTotalTime(id: String, username: String, time: Long)

    fun addLoveTime(id: String, username: String, time: Long)

    fun clearWeekTime()

}