package ru.sexit.platform.domain.service

import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Isolation
import org.springframework.transaction.annotation.Transactional
import ru.sexit.platform.api.http.profile.PsychoProfileRequest
import ru.sexit.platform.domain.model.PsychoProfile
import ru.sexit.platform.domain.model.PsychoRating
import ru.sexit.platform.domain.repo.FeedbackRepo
import ru.sexit.platform.domain.repo.PsychoProfileRepo
import ru.sexit.platform.infrastructure.AlreadyExistException
import ru.sexit.platform.infrastructure.NotFoundException
import ru.sexit.platform.utils.log

@Service
class PsychoProfileService(
    private val psychoProfileRepo: PsychoProfileRepo,
    private val feedbackRepo: FeedbackRepo,
) {
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    fun updateOrCreate(id: Long, request: PsychoProfileRequest): PsychoProfile {
        if (id == 0L) {
            return create(request)
                .also { log.info("Profile (id: ${it.id}) has been created") }
        }

        return update(id, request)
            .also { log.info("Profile (id: $id) has been updated") }
    }

    private fun create(request: PsychoProfileRequest): PsychoProfile {
        val existedProfile = psychoProfileRepo.findByEmail(request.email)
        if (existedProfile != null) {
            throw AlreadyExistException("Psycho profile with such email already exists")
        }

        return psychoProfileRepo.save(
            PsychoProfile(
                id = 0L,
                name = request.name,
                email = request.email,
                price = request.price,
                isFirstFree = request.isFirstFree,
                bio = request.bio,
            )
        )
    }

    private fun update(id: Long, request: PsychoProfileRequest): PsychoProfile {
        val profile = getProfileById(id)
        val profileByEmail = psychoProfileRepo.findByEmail(request.email)

        if (profileByEmail != null && profile.id != profileByEmail.id) {
            throw AlreadyExistException("Psycho profile with such email already exists")
        }

        with(profile) {
            name = request.name
            email = request.email
            price = request.price
            isFirstFree = request.isFirstFree
            bio = request.bio
        }

        return psychoProfileRepo.save(profile)
    }

    fun getProfileById(id: Long): PsychoProfile = psychoProfileRepo.findById(id)
            .orElseThrow { NotFoundException("No such profile exists") }

    fun getPsychoRatings(id: Long): PsychoRating {
        val feedbacks = feedbackRepo.findAllByPsychoProfileId(id)
        val number = feedbacks.size

        if (number == 0) {
            return PsychoRating(
                rating = 0.0,
                feedbacks = emptyList(),
            )
        }

        return PsychoRating(
            rating = feedbacks.sumOf { it.rating }.toDouble() / number,
            feedbacks = feedbacks,
        )
    }
}
