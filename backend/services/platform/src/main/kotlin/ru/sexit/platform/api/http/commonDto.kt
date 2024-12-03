package ru.sexit.platform.api.http

import org.springframework.data.domain.Page

data class UserInfoDto(
    val id: String,
    val email: String,
    val username: String,
    val name: String,
    val surname: String,
    val patronymic: String?,
)

data class PageView<T>(
    val totalElements: Long,
    val totalPages: Int,
    val content: List<T>,
)

fun <T> Page<T>.toView(): PageView<T> = PageView(
    totalElements = totalElements,
    totalPages = totalPages,
    content = content
)

fun <T, R> Page<T>.toView(mapper: (T) -> R): PageView<R> = PageView(
    totalElements = totalElements,
    totalPages = totalPages,
    content = content.map { mapper.invoke(it) }
)
