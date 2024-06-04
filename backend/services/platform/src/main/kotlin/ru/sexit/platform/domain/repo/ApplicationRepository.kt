package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.AcceptedApplication
import ru.sexit.platform.domain.model.ApplicationEntity

@Repository
interface ApplicationRepository: JpaRepository<ApplicationEntity, Long> {

    @Query(
        value = """
            select distinct ap from ApplicationEntity ap 
            left join ap.slot sl 
            left join sl.psychoProfile ps
            where ps.id = :psychoId
            and ap.status = 'NEED_REVIEW'
        """
    )
    fun findApplicationsByPsychoId(psychoId: Long): List<ApplicationEntity>?


    @Query(
        value = """
            select distinct new ru.sexit.platform.domain.model.AcceptedApplication(ap.id, ps.id, ps.name, ps.price, sl.id, sl.time, sl.monthId, sl.dayId, sl.yearId, ap.status, ap.anonType, ap.visitType, ap.link, ap.address, ap.results) from ApplicationEntity ap  
            left join ap.slot sl 
            left join sl.psychoProfile ps 
            where lower(ps.name) like concat('%', lower(cast(:psychoName as string)), '%')  
            and (ap.status = 'DONE' or ap.status = 'PLANNED')
        """
    )
    fun findAcceptedApplicationsByPsychoName(psychoName: String): List<AcceptedApplication>

}
