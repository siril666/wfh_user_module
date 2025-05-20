package com.wfo_exception_tracker.userservice.controller

import com.wfo_exception_tracker.userservice.dto.UserDto
import com.wfo_exception_tracker.userservice.entity.UserLogin
import com.wfo_exception_tracker.userservice.entity.UserRegistration
import com.wfo_exception_tracker.userservice.repository.UserRegisterRepository
import com.wfo_exception_tracker.userservice.service.UserService
import io.jsonwebtoken.Claims
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*
import kotlin.math.log

@RestController

@RequestMapping("/users")
@CrossOrigin (origins =  ["http://localhost:5173","http://localhost:5174"])
class UserController(
    private val userService: UserService,
    private val userRegisterRepository: UserRegisterRepository
) {

    @GetMapping
    fun getAllUsers(): MutableList<UserLogin?> {
        return userService.getAllUsers()
    }

    @GetMapping("/currentUser")
    fun getCurrentUser(): ResponseEntity<UserLogin> {
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.credentials as Claims

        val uid = claims["uid"] as String
        val user = userService.getUser(uid)

        println(user)
        return ResponseEntity.ok(user)
    }


    @GetMapping("/profile")
    fun getUserProfile(): UserRegistration? {
        val authentication = SecurityContextHolder.getContext().authentication
        val claims = authentication.credentials as Claims

        val ibsEmpId = claims.subject.toLong()

        println(ibsEmpId)
        return userRegisterRepository.findByIbsEmpId(ibsEmpId)

    }

    @PostMapping("/update")
    fun updateUserProfile(@RequestBody updatedProfile: UserRegistration): ResponseEntity<String> {
        val existingUser = userRegisterRepository.findByIbsEmpId(updatedProfile.ibsEmpId)
        return if (existingUser != null) {
            updatedProfile.ibsEmpId = existingUser.ibsEmpId // ensure existing ID is retained
            userRegisterRepository.save(updatedProfile)
            ResponseEntity.ok("User profile updated successfully")
        } else {
            ResponseEntity.notFound().build()
        }
    }

//    @GetMapping("/role")
//    fun getRoleByEmployeeId(): ResponseEntity<Any> {
//
//        val authentication = SecurityContextHolder.getContext().authentication
//        val claims = authentication.credentials as Claims
//
//        val ibsEmpId = claims.subject.toLong()
//
//
//        val role = userService.getRoleByEmployeeId(ibsEmpId)
//        return if (role != null) {
//            ResponseEntity.ok(role)
//        } else {
//            ResponseEntity.notFound().build()
//        }
//    }
}
