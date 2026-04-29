package com.statsbot.member.infrastructure.mappers

import com.statsbot.member.domain.model.Member
import com.statsbot.member.infrastructure.persistence.entity.MemberJpaEntity
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface MemberMapper {

    fun toModel(entity: MemberJpaEntity): Member

}