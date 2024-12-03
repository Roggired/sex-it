package ru.sexit.platform.api.http.applications

data class NewApplicationRequest(
    val slotId: Long,
    val anonType: AnonType,
    val visitType: VisitType,
    val description: String?
)

data class AcceptApplicationRequest(
    val address: String?
)

data class NoteRequest(
    val note: String = ""
)
