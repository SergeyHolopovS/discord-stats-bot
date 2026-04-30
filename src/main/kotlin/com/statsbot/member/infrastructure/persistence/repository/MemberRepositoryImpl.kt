package com.statsbot.member.infrastructure.persistence.repository

import com.statsbot.member.domain.model.Member
import com.statsbot.member.domain.repository.MemberRepository
import com.statsbot.member.infrastructure.mappers.MemberMapper
import com.statsbot.member.infrastructure.persistence.entity.MemberJpaEntity
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
class MemberRepositoryImpl(
    private val repository: MemberJpaRepository,
    private val mapper: MemberMapper
) : MemberRepository {

    override fun save(
        userId: String,
        username: String,
        currentVoice: String?,
        joinedAt: Instant?
    ): Member
        = mapper.toModel(
            repository.save(
                MemberJpaEntity(
                    id = userId,
                    username = username,
                    currentVoice = currentVoice,
                    joinedVoiceAt = joinedAt,
                )
            )
        )

    override fun findById(userId: String): Member
        = mapper.toModel(
            repository
                .findById(userId)
                .orElseThrow { RuntimeException("Ошибка поиска по id") }
        )

    override fun getTop10(weekly: Boolean): List<Member>
        = (
            if (weekly) repository.findTop10ByOrderByWeekTimeDesc()
            else repository.findTop10ByOrderByTotalTimeDesc()
        ).map { el -> mapper.toModel(el) }

    override fun getTotalTime(id: String, username: String, weekly: Boolean): Long {
        val member = repository.findById(id)

        if (member.isEmpty) {
            repository.save(MemberJpaEntity(id = id, username = username))
            return 0L
        }
        return if (weekly) member.get().weekTime else member.get().totalTime
    }

    override fun getLoveTime(id: String, username: String): Long {
        val member = repository.findById(id)

        if (member.isEmpty) {
            repository.save(MemberJpaEntity(id = id, username = username))
            return 0L
        }
        return member.get().loveTime
    }

    @Transactional
    override fun addTotalTime(id: String, time: Long)
        = repository.incrementTotalTime(id, time)

    @Transactional
    override fun addWeekTime(id: String, time: Long)
        = repository.incrementWeekTime(id, time)

    @Transactional
    override fun addLoveTime(id: String, time: Long)
        = repository.incrementLoveTime(id, time)

    @Transactional
    override fun clearWeekTime()
        = repository.resetWeekTime()

    @Transactional
    override fun setJoinedAt(userId: String, date: Instant)
        = repository.updateJoinedVoiceAt(userId, date)

    @Transactional
    override fun setCurrentChannel(userId: String, channelId: String)
        = repository.updateCurrentVoice(userId, channelId)

    @Transactional
    override fun clearJoinedAt(userId: String)
        = repository.clearJoinedVoiceAt(userId)

    @Transactional
    override fun clearCurrentChannel(userId: String)
        = repository.clearCurrentVoice(userId)

    override fun existsById(userId: String): Boolean
        = repository.existsById(userId)

}