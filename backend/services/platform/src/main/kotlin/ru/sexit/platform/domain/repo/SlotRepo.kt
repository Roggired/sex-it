package ru.sexit.platform.domain.repo

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import ru.sexit.platform.domain.model.Slot
import java.time.LocalTime

@Repository
interface SlotRepo: JpaRepository<Slot, Long> {
    @Query(
        """
            SELECT COUNT(*) > 0 FROM Slot s
            WHERE s.yearId = :yearId AND s.monthId = :monthId AND s.dayId = :dayId
            AND s.time >= :timeFrom AND s.time <= :timeTo
        """
    )
    fun isSlotAlreadyCaptured(
        yearId: Int,
        monthId: Int,
        dayId: Int,
        timeFrom: LocalTime,
        timeTo: LocalTime,
    ): Boolean

    @Query(
        """
            SELECT DISTINCT s FROM Slot s
            WHERE s.yearId = :yearId 
                AND s.monthId = :monthId
                AND (COALESCE(:dayId, NULL) IS NULL OR s.dayId = :dayId)
                AND s.psychoProfile.id = :psychoId
            ORDER BY s.dayId, s.time
        """
    )
    fun findAllSlotsByMonthOrDayForPsycho(
        psychoId: Long,
        yearId: Int,
        monthId: Int,
        dayId: Int? = null
    ): List<Slot> // TODO calc status based on applications

    @Query(
        """
            SELECT s FROM Slot s
            WHERE 
                s.yearId = :yearId 
                AND s.monthId = :monthId
                AND (COALESCE(:dayId, NULL) IS NULL OR s.dayId = :dayId)
                AND s.psychoProfile.id = :psychoId
            ORDER BY s.dayId, s.time
        """
    )
    fun findAllSlotsByMonthOrDayForClient(
        psychoId: Long,
        yearId: Int,
        monthId: Int,
        dayId: Int? = null
    ): List<Slot> // TODO calc status based on applications
}
