package ru.sexit.platform.utils

import java.time.LocalDateTime
import java.time.Month
import java.time.ZoneId

fun currentUTCTime(): LocalDateTime = LocalDateTime.now(ZoneId.of("UTC"))
fun infinityUTCTime(): LocalDateTime = LocalDateTime.of(10000, Month.JANUARY, 1, 0, 0)
