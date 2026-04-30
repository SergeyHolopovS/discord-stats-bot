package com.statsbot.member.infrastructure.persistence.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import java.time.Instant

@Entity
data class MemberJpaEntity(

    @Id
    @Column(unique = true, nullable = false)
    var id: String,

    @Column(unique = true, nullable = false)
    var username: String,

    @Column(unique = false, nullable = false)
    var weekTime: Long = 0,

    @Column(unique = false, nullable = false)
    var totalTime: Long = 0,

    @Column(unique = false, nullable = false)
    var loveTime: Long = 0,

    @Column(unique = false, nullable = true)
    var joinedVoiceAt: Instant? = null,

    @Column(unique = false, nullable = true)
    var currentVoice: String? = null,

)
