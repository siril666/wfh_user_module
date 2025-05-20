package com.wfo_exception_tracker.userservice.repository

import com.wfo_exception_tracker.userservice.entity.UserLogin
import com.wfo_exception_tracker.userservice.entity.UserRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserLoginRepository : JpaRepository<UserLogin?, String> {
    fun findByIbsEmpId(ibsEmpId: Long?): UserLogin?
}