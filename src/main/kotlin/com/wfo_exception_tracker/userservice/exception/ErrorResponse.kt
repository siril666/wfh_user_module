package com.wfo_exception_tracker.userservice.exception

import java.time.LocalDateTime


data class ErrorResponse(
    val timestamp: LocalDateTime,
    val status: Int,
    val error: String,
    val message: String
)