package ru.sexit.platform.domain.model

import jakarta.persistence.*
import java.util.UUID

@Entity
@Table(name = "bbb_meetings")
class BbbMeeting(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,
    val uuid: UUID,
    val psychoProfileId: Long,
    val applicationId: Long,
)
