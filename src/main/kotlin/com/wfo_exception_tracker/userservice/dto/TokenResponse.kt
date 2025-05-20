package com.wfo_exception_tracker.userservice.dto

import com.wfo_exception_tracker.userservice.entity.UserRegistration


data class TokenResponse(
    val accessToken: String,
    val userInfo : UserRegistration
)
