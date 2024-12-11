package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.api.http.applications.VisitType
import ru.sexit.platform.domain.model.AcceptedApplication
import ru.sexit.platform.domain.model.Application
import ru.sexit.platform.domain.model.SlotStatus
import java.time.LocalDateTime

@Repository
interface ApplicationRepository: JpaRepository<Application, Long> {

    @Query(
        value = """
            select distinct ap from Application ap 
            left join ap.slot sl 
            left join sl.psychoProfile ps
            where ps.id = :psychoId
            and ap.status = 'NEED_REVIEW'
        """
    )
    fun findApplicationsByPsychoId(psychoId: Long): List<Application>?


    @Query(
        value = """
            select distinct new ru.sexit.platform.domain.model.AcceptedApplication(ap.id, ps.id, ps.name, ps.price, sl.id, sl.time, sl.monthId, sl.dayId, sl.yearId, ap.status, ap.anonType, ap.visitType, ap.link, ap.address, ap.results,  ap.friendId, ap.friendName, ap.referId) from Application ap  
            left join ap.slot sl 
            left join sl.psychoProfile ps 
            where (coalesce(:psychoName, null) is null or lower(ps.name) like lower(concat('%', cast(:psychoName as string), '%'))) 
                and (
                    (coalesce(:appStatus2, null) is null and ap.status = :appStatus1)
                    or (coalesce(:appStatus1, null) is not null and (ap.status = :appStatus1 or ap.status = :appStatus2))
                )
        """
    )
    fun findAcceptedApplicationsByPsychoNameAndAppStatus1AndAppStatus2(
        psychoName: String?,
        appStatus1: SlotStatus,
        appStatus2: SlotStatus?,
    ): List<AcceptedApplication>

    fun countBySlotPsychoProfileIdAndStatusAndVisitTypeAndSlotYearIdAndSlotMonthId(
        psychoId: Long,
        status: SlotStatus,
        visitType: VisitType,
        yearId: Int,
        monthId: Int,
    ): Int

    @Query(
        """
            SELECT COUNT(*)
            FROM Application a
            WHERE a.creationTime BETWEEN :from AND :to AND a.status = :status AND a.visitType = :visitType
        """
    )
    fun countPerformedAtPeriod(
        from: LocalDateTime,
        to: LocalDateTime,
        visitType: VisitType = VisitType.ONLINE,
        status: SlotStatus = SlotStatus.DONE,
    ): Int
}
