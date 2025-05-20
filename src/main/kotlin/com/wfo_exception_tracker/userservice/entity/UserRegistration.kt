package com.wfo_exception_tracker.userservice.entity


import jakarta.persistence.*
import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size
import lombok.AllArgsConstructor
import lombok.Data
import lombok.NoArgsConstructor

@Entity(name = "employee_info")
@Data
@NoArgsConstructor
@AllArgsConstructor
data class UserRegistration(
    @Id
    @Column(name = "ibs_emp_id")
    var ibsEmpId: Long,

    @field:NotBlank
    @Column(name = "user_name")
    val userName: String,

    @field:NotBlank
    val role: String,

    @field:Email
    @field:NotBlank
    @Column(name = "email_id", unique = true)
    val emailId: String,

    @Column(name = "phone_number")
    val phoneNumber: Long,

    val location: String,

    val country: String,

    @Column(name = "admin_verified")
    val adminVerified: Boolean = false
)
