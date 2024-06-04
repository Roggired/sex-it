package ru.sexit.platform.api.http.applications

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.sexit.platform.domain.service.ApplicationService

@RestController
@RequestMapping("/api/v1/applications")
class ApplicationController(
    private val applicationService: ApplicationService
) {

    @PostMapping
    fun createApplication(
        @RequestBody applicationRequest: NewApplicationRequest
    ): ApplicationViewCreated {
        return applicationService.createApplication(applicationRequest)
    }

    @PostMapping("/{id}/accept")
    fun acceptApplication(
        @PathVariable("id") id: Long
    ) {
        applicationService.acceptApplication(id)
    }

    @PostMapping("/{id}/reject")
    fun rejectApplication(
        @PathVariable("id") id: Long
    ) {
        applicationService.rejectApplication(id)
    }

    @GetMapping
    fun getApplicationByPsychoId(
        @RequestParam(name = "psychoId") psychoId: Long
    ): List<ApplicationWithClientView> {
        return applicationService.getByPsychoId(psychoId)
            .map {
                ApplicationWithClientView(
                    id = it.id,
                    clientName = "Иван Иванович",
                    slot = it.slot,
                    creationTime = it.creationTime,
                    anonType = it.anonType,
                    visitType = it.visitType,
                    status = it.status,
                    description = it.description,
                    link = it.link,
                    address = it.address,
                )
            }
    }

    @GetMapping("/accepted")
    fun getAcceptedApplication(
        @RequestParam("psychoName") psychoName: String
    ): List<AcceptedApplicationView> {
        return applicationService.getAcceptedApplicationsByPsychoName(psychoName)
    }
}
