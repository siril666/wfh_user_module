package com.wfo_exception_tracker.userservice.repository

import com.wfo_exception_tracker.userservice.entity.UserLogin
import com.wfo_exception_tracker.userservice.entity.UserRegistration
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRegisterRepository : JpaRepository<UserRegistration?, Long> {
    fun findByIbsEmpId(ibsEmpId: Long?): UserRegistration?
}