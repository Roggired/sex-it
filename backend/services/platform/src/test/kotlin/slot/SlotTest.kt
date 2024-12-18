package slot

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.DescribeSpec
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import ru.sexit.platform.domain.repo.SlotRepo
import ru.sexit.platform.domain.service.SlotService
import ru.sexit.platform.api.http.slot.SlotRequest
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.model.Slot
import ru.sexit.platform.domain.service.BbbMeetingService
import ru.sexit.platform.domain.service.PsychoProfileService
import ru.sexit.platform.infrastructure.exception.AlreadyExistException
import java.time.LocalTime

class SlotTest : DescribeSpec({
    val slotRepo = mockk<SlotRepo>()
    val bbbMeetingService = mockk<BbbMeetingService>()
    val psychoProfileService = mockk<PsychoProfileService>()

    val slotService = SlotService(bbbMeetingService, slotRepo, psychoProfileService)

    describe("SlotService.createSlot") {

        context("success creating slot") {
            val slotRequest = SlotRequest(
                time = LocalTime.of(10, 0),
                yearId = 2024,
                monthId = 5,
                dayId = 15
            )

            val psychoProfile = PsychoProfile(
                id = 1L,
                userId = "123",
                name = "Маша",
                email = "masha@example.com",
                price = 10,
                isFirstFree = true,
                bio = "pizdec"
            )
            val createdSlot = Slot(
                id = 1L,
                yearId = 2024,
                monthId = 5,
                dayId = 15,
                time = LocalTime.of(10, 0)
            )
            createdSlot.psychoProfile = psychoProfile

            every { psychoProfileService.getMyProfile() } returns psychoProfile
            every { slotRepo.isSlotAlreadyCaptured(any(), any(), any(), any(), any(), any()) } returns false
            every { slotRepo.save(any()) } returns createdSlot
            it("should create new slot") {
                val createdSlot = slotService.createSlot(slotRequest)

                createdSlot.yearId shouldBe 2024
                createdSlot.monthId shouldBe 5
                createdSlot.dayId shouldBe 15
                createdSlot.time shouldBe LocalTime.of(10, 0)

                verify { slotRepo.save(any()) }
            }
        }

        context("slot already exists") {
            val slotRequest = SlotRequest(
                time = LocalTime.of(10, 0),
                yearId = 2024,
                monthId = 5,
                dayId = 15
            )

            val psychoProfile = PsychoProfile(
                id = 1L,
                userId = "123",
                name = "Маша",
                email = "masha@example.com",
                price = 10,
                isFirstFree = true,
                bio = "pizdec"
            )
            every { psychoProfileService.getMyProfile() } returns psychoProfile
            every { slotRepo.isSlotAlreadyCaptured(any(), any(), any(), any(), any(), any()) } returns true

            it("should throw AlreadyExistException") {
                shouldThrow<AlreadyExistException> {
                    slotService.createSlot(slotRequest)
                }
            }
        }
    }
})
