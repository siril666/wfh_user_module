package com.wfo_exception_tracker.userservice.dto

import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Size

data class TokenRequestDto(
    @field:NotNull
    val ibsEmpId: Long,

    @field:Size(min = 8)
    val password: String
)