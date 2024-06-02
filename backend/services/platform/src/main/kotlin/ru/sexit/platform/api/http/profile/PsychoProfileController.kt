package ru.sexit.platform.api.http.profile

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.domain.service.PsychoProfileService

@RestController
@RequestMapping("/api/v1/profiles")
class PsychoProfileController(
    private val profileService: PsychoProfileService,
) {
    @PatchMapping("/{id}")
    fun updateOrCreate(
        @PathVariable id: Long,
        @RequestBody @Validated request: PsychoProfileRequest,
    ): ProfilePsychoView = profileService.updateOrCreate(
        id = id,
        request = request,
    ).toPsychoView()

    @GetMapping("/{id}")
    fun get(
        @PathVariable id: Long,
        mode: ProfileViewMode,
    ): Any {
        val profile = profileService.getProfileById(id)
        return when(mode) {
            ProfileViewMode.CLIENT -> profile.toClientView(
                psychoRating = profileService.getPsychoRatings(id)
            )
            ProfileViewMode.PSYCHO -> profile.toPsychoView()
        }
    }
}
