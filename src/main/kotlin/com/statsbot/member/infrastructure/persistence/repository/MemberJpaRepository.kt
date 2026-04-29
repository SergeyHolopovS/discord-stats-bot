package com.statsbot.member.infrastructure.persistence.repository

import com.statsbot.member.infrastructure.persistence.entity.MemberJpaEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional

interface MemberJpaRepository : JpaRepository<MemberJpaEntity, String> {

    fun findTop10ByOrderByTotalTimeDesc(): List<MemberJpaEntity>

    fun findTop10ByOrderByWeekTimeDesc(): List<MemberJpaEntity>

    @Modifying
    @Transactional
    @Query("UPDATE MemberJpaEntity m SET m.weekTime = 0")
    fun resetWeekTime()

}