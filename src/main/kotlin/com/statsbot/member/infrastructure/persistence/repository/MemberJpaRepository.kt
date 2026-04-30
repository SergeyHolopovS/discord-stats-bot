package com.statsbot.member.infrastructure.persistence.repository

import com.statsbot.member.infrastructure.persistence.entity.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, String> {

    fun findTop10ByOrderByTotalTimeDesc(): List<MemberJpaEntity>

    fun findTop10ByOrderByWeekTimeDesc(): List<MemberJpaEntity>

    @Modifying
    @Transactional
    @Query("UPDATE MemberJpaEntity m SET m.weekTime = 0")
    fun resetWeekTime()

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.weekTime = m.weekTime + :delta WHERE m.id = :id")
    fun incrementWeekTime(id: String, delta: Long)

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.totalTime = m.totalTime + :delta WHERE m.id = :id")
    fun incrementTotalTime(id: String, delta: Long)

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.loveTime = m.loveTime + :delta WHERE m.id = :id")
    fun incrementLoveTime(id: String, delta: Long)

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.joinedVoiceAt = :joinedVoiceAt WHERE m.id = :id")
    fun updateJoinedVoiceAt(id: String, joinedVoiceAt: Instant)

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.currentVoice = :currentVoice WHERE m.id = :id")
    fun updateCurrentVoice(id: String, currentVoice: String)

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.joinedVoiceAt = null WHERE m.id = :id")
    fun clearJoinedVoiceAt(id: String)

    @Modifying
    @Query("UPDATE MemberJpaEntity m SET m.currentVoice = null WHERE m.id = :id")
    fun clearCurrentVoice(id: String)

}