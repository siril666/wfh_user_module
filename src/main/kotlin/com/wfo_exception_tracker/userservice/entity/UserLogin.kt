//package com.wfo_exception_tracker.userservice.entity
//
//import com.fasterxml.jackson.annotation.JsonIncludeProperties
//import jakarta.persistence.*
//import jakarta.validation.constraints.Size
//import lombok.AllArgsConstructor
//import lombok.Data
//import lombok.NoArgsConstructor
//import org.springframework.security.core.GrantedAuthority
//import org.springframework.security.core.authority.SimpleGrantedAuthority
//import org.springframework.security.core.userdetails.UserDetails
//
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@Entity(name = "users")
//@JsonIncludeProperties("uid", "ibsEmpId", "password", "roles")
//class UserLogin : UserDetails {
//    @Id
//    @GeneratedValue(strategy = GenerationType.UUID)
//    private val uid: String? = null
//
//    @Column(unique = true)
//    private var ibsEmpId: Long,
//
//    private val password: @Size(min = 8) String? = null
//    private val roles: List<String>? = null
//
//
//
//    override fun getAuthorities(): Collection<GrantedAuthority?> {
//        return roles!!.stream()
//            .map { role: String? -> SimpleGrantedAuthority(role) }
//            .toList()
//    }
//
//    override fun getPassword(): String? {
//        return password
//    }
//
//    val role: String
//        get() = roles!![0]
//
//}


package com.wfo_exception_tracker.userservice.entity

import com.fasterxml.jackson.annotation.JsonIncludeProperties
import jakarta.persistence.*
import jakarta.validation.constraints.Size
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
@Entity(name = "users")
@JsonIncludeProperties("uid", "ibsEmpId", "password", "roles")
class UserLogin(

    @Column(unique = true)
    var ibsEmpId: Long,

    @field:Size(min = 8)
    private var password: String? = null,

    private var roles: List<String>? = null

) : UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private var uid: String? = null

    fun getUid(): String? {
        return uid
    }
    override fun getAuthorities(): Collection<GrantedAuthority> {
        return roles?.map { SimpleGrantedAuthority(it) } ?: emptyList()
    }

    override fun getPassword(): String? = password

    override fun getUsername(): String = ibsEmpId.toString()

    fun setPassword(password: String) {
        this.password = password
    }
    override fun isAccountNonExpired(): Boolean = true
    override fun isAccountNonLocked(): Boolean = true
    override fun isCredentialsNonExpired(): Boolean = true
    override fun isEnabled(): Boolean = true

    val role: String
        get() = roles?.firstOrNull() ?: ""
}
