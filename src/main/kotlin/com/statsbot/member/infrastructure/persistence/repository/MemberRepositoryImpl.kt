package com.statsbot.member.infrastructure.persistence.repository

import com.statsbot.member.domain.model.Member
import com.statsbot.member.domain.repository.MemberRepository
import com.statsbot.member.infrastructure.mappers.MemberMapper
import com.statsbot.member.infrastructure.persistence.entity.MemberJpaEntity
import org.springframework.stereotype.Repository

@Repository
class MemberRepositoryImpl(
    private val repository: MemberJpaRepository,
    private val mapper: MemberMapper
) : MemberRepository {

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

    override fun allTotalTime(id: String, username: String, time: Long) {
        val memberOpt = repository.findById(id)

        if (memberOpt.isEmpty) repository.save(
            MemberJpaEntity(
                id = id,
                username = username,
                totalTime = time,
                weekTime = time,
            )
        )
        else {
            val member = memberOpt.get()
            member.totalTime += time
            member.weekTime += time
            repository.save(member)
        }
    }

    override fun addLoveTime(id: String, username: String, time: Long) {
        val memberOpt = repository.findById(id)

        if (memberOpt.isEmpty) repository.save(MemberJpaEntity(id = id, username = username, loveTime = time))
        else {
            val member = memberOpt.get()
            member.loveTime += time
            member.totalTime += time
            member.weekTime += time
            repository.save(member)
        }
    }

    override fun clearWeekTime()
        = repository.resetWeekTime()

}