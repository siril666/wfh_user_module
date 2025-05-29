package com.wfo_exception_tracker.userservice.dto

data class ChangePasswordRequest(
    val currentPassword: String,
    val newPassword: String,
    val confirmNewPassword: String
)