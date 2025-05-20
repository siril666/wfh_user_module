package com.wfo_exception_tracker.userservice.controller

import com.wfo_exception_tracker.userservice.dto.TokenRequestDto
import com.wfo_exception_tracker.userservice.dto.TokenResponse
import com.wfo_exception_tracker.userservice.dto.UserDto
import com.wfo_exception_tracker.userservice.exception.UnverifiedUserException
import com.wfo_exception_tracker.userservice.repository.UserRegisterRepository
import com.wfo_exception_tracker.userservice.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.web.bind.annotation.*


@RestController
@CrossOrigin (origins =  ["http://localhost:5173","http://localhost:5174"])
@RequestMapping("/auth")
class AuthController(
    private val userService: UserService,
    private val userRegisterRepository: UserRegisterRepository
) {

    @PostMapping
    fun createUser(@Valid @RequestBody user: UserDto): ResponseEntity<String> {

        return try {

            if (userService.getUserRegistrationByIbsEmpId(user.ibsEmpId) != null) {
                return ResponseEntity("Employee ID already exists", HttpStatus.CONFLICT)
            }

            userService.createRegisterUser(user)

            ResponseEntity("User registered successfully", HttpStatus.CREATED)
        } catch (e: Exception) {
            ResponseEntity("Registration failed", HttpStatus.INTERNAL_SERVER_ERROR)
        }
    }

    @PostMapping("/token")
    fun generateToken(@Valid @RequestBody tokenRequestDto: TokenRequestDto): ResponseEntity<TokenResponse> {
        if (!userService.isUserVerifiedByAdmin(tokenRequestDto.ibsEmpId)) {
            throw UnverifiedUserException("User with Emp ID ${tokenRequestDto.ibsEmpId} is not verified by admin.")
        }

        val token = userService.generateTokenForUser(tokenRequestDto)

        val employeeInfo = userRegisterRepository.findByIbsEmpId(tokenRequestDto.ibsEmpId)
            ?: throw IllegalStateException("User with Emp ID ${tokenRequestDto.ibsEmpId} not found in database.")

        return ResponseEntity.status(HttpStatus.CREATED).body(
            TokenResponse(
                accessToken = token,
                userInfo = employeeInfo
            )
        )
    }


}