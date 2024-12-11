package ru.sexit.platform.api.http.applications

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.sexit.platform.domain.model.Application
import ru.sexit.platform.domain.model.SlotStatus
import ru.sexit.platform.domain.model.toView
import ru.sexit.platform.domain.service.ApplicationService

@RestController
@RequestMapping("/api/v1/applications")
class ApplicationController(
    private val applicationService: ApplicationService
) {

    @PostMapping
    fun createApplication(
        @RequestParam("referId") referId: Long?,
        @RequestBody applicationRequest: NewApplicationRequest
    ): ApplicationView = applicationService.createApplication(applicationRequest, referId).toView()

    @PostMapping("/{id}/accept")
    fun acceptApplication(
        @PathVariable("id") id: Long,
        @RequestBody @Validated request: AcceptApplicationRequest,
    ): Unit = applicationService.acceptApplication(id, request)

    @PostMapping("/{id}/reject")
    fun rejectApplication(
        @PathVariable("id") id: Long,
    ): Unit = applicationService.rejectApplication(id)

    @PostMapping("/{id}/revoke")
    fun revokeApplication(
        @PathVariable("id") id: Long,
    ): Unit = applicationService.revokeApplication(id)

    @GetMapping("/{id}")
    fun getApplicationById(
        @PathVariable("id") id: Long
    ): ApplicationView = applicationService.getById(id).toView()

    @GetMapping
    fun getApplicationByPsychoId(
        @RequestParam(name = "psychoId") psychoId: Long
    ): List<ApplicationWithClientView> = applicationService.getByPsychoId(psychoId)

    @GetMapping("/accepted")
    fun getAcceptedApplication(
        @RequestParam(required = false) psychoName: String? = null,
        @RequestParam(required = false) appStatus: SlotStatus? = null
    ): List<AcceptedApplicationView> = applicationService.getAcceptedApplications(psychoName, appStatus)

    @PatchMapping("/{id}/note")
    fun patchApplicationNote(
        @PathVariable("id") id: Long,
        @RequestBody @Validated request: NoteRequest,
    ): Unit = applicationService.patchApplicationNote(id, request)

    @PostMapping("/{id}/finish")
    fun finishApplication(
        @PathVariable("id") id: Long,
        @RequestBody @Validated request: NoteRequest,
    ): Unit = applicationService.finishApplication(id, request)
}
