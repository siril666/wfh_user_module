package com.wfo_exception_tracker.userservice.service

import com.wfo_exception_tracker.userservice.dto.ChangePasswordRequest
import com.wfo_exception_tracker.userservice.dto.TokenRequestDto
import com.wfo_exception_tracker.userservice.dto.TokenResponse
import com.wfo_exception_tracker.userservice.dto.UserDto
import com.wfo_exception_tracker.userservice.entity.UserLogin
import com.wfo_exception_tracker.userservice.entity.UserRegistration
import com.wfo_exception_tracker.userservice.repository.UserLoginRepository
import com.wfo_exception_tracker.userservice.repository.UserRegisterRepository
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userLoginRepository: UserLoginRepository,
    private val userRegisterRepository: UserRegisterRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService
) {

    fun userDetailsService(): UserDetailsService {
        return UserDetailsService { username ->
            // username is ibsEmpId in string form, so parse it to Long
            val ibsEmpId = username.toLongOrNull()
                ?: throw UsernameNotFoundException("Invalid employee ID format")
            userLoginRepository.findByIbsEmpId(ibsEmpId)
                ?: throw UsernameNotFoundException("User not found with IBS Emp ID: $ibsEmpId")
        }
    }

    fun getUser(uid: String): UserLogin? {
        return userLoginRepository.findById(uid)
            .orElseThrow { UsernameNotFoundException("User not found") }
    }

    fun getAllUsers(): MutableList<UserLogin?> {
        return userLoginRepository.findAll()
    }

    fun createRegisterUser(userDto: UserDto): UserDto {

        val encryptedPassword = passwordEncoder.encode(userDto.password)
        userDto.password=encryptedPassword


        val userRegistration = UserRegistration(
            ibsEmpId = userDto.ibsEmpId,
            userName = userDto.userName,
            role = userDto.role,
            emailId = userDto.emailId,
            phoneNumber = userDto.phoneNumber,
            location = userDto.location,
            country = userDto.country,
            adminVerified = false
        )


        val user = UserLogin(
            ibsEmpId = userDto.ibsEmpId,
            password = userDto.password,
            roles = listOf("employee")
        )
        userRegisterRepository.save(userRegistration)
        userLoginRepository.save(user)
        return userDto
    }

    fun isUserVerifiedByAdmin(ibsEmpId: Long): Boolean {
        val user = userRegisterRepository.findByIbsEmpId(ibsEmpId)
            ?: throw UsernameNotFoundException("User not found with Emp ID $ibsEmpId")
        return user.adminVerified
    }

    fun generateTokenForUser(tokenRequestDto: TokenRequestDto): String {
        val user = userLoginRepository.findByIbsEmpId(tokenRequestDto.ibsEmpId)
            ?: throw UsernameNotFoundException("User not found with Emp ID ${tokenRequestDto.ibsEmpId}")

        if (!passwordEncoder.matches(tokenRequestDto.password, user.password)) {
            throw BadCredentialsException("Invalid credentials")
        }

        val token = jwtService.createToken(user)
        return token
    }



    fun getUserRegistrationByIbsEmpId(ibsEmpId: Long): UserRegistration? {
        return userRegisterRepository.findByIbsEmpId(ibsEmpId)

    }

    fun getRoleByEmployeeId(employeeId: Long): Any? {
        val user = userRegisterRepository.findByIbsEmpId(employeeId)
        return if (user != null) {
            user.role
        } else {
            null
        }
    }

    fun updatePassword(ibsEmpId: Long, request: ChangePasswordRequest): String {
        val user = userLoginRepository.findByIbsEmpId(ibsEmpId)
            ?: throw UsernameNotFoundException("User not found")

        if (!passwordEncoder.matches(request.currentPassword, user.password)) {
            throw BadCredentialsException("Current password is incorrect")
        }

        if (request.newPassword != request.confirmNewPassword) {
            throw IllegalArgumentException("New passwords don't match")
        }

        user.setPassword( passwordEncoder.encode(request.newPassword))
        userLoginRepository.save(user)
        return "Password updated successfully"
    }

}
