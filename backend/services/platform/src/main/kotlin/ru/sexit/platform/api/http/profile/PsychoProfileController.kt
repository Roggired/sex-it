package ru.sexit.platform.api.http.profile

import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import ru.sexit.platform.api.http.PageView
import ru.sexit.platform.api.http.toView
import ru.sexit.platform.domain.model.toView
import ru.sexit.platform.domain.service.PsychoProfileService
import ru.sexit.platform.utils.RequestMode

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
        mode: RequestMode,
    ): Any {
        val profile = profileService.getProfileById(id)
        return when(mode) {
            RequestMode.CLIENT -> profile.toClientView(
                psychoRating = profileService.getPsychoRatings(id)
            )
            RequestMode.PSYCHO -> profile.toPsychoView()
        }
    }

    @GetMapping("/my")
    fun getMyProfile(): ProfilePsychoView = profileService.getMyProfile().toPsychoView()

    @PostMapping("/filtered")
    fun filterProfiles(
        @RequestParam(required = false) pageNumber: Int? = 0,
        @RequestParam(required = false) pageSize: Int? = 6,
        @RequestBody @Validated request: FilterPsychoRequest,
    ): PageView<PsychoProfileForCatalogueView> = profileService.filterPsychoProfiles(
        request = request,
        pageNumber = pageNumber ?: 0,
        pageSize = pageSize ?: 6,
    ).toView { it.toView() }
}
