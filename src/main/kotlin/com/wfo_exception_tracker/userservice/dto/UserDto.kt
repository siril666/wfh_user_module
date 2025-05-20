//package com.wfo_exception_tracker.userservice.dto
//
//data class UserDto(
//    var uid: Long? = null,
//    var name: String? = null,
//    var ibsEmpId: Long,
//    var password :String? = null,
//    var role: String? = null,
//    var roles: List<String>? = null,
//    var email: String? = null,
//    var phoneNumber: String? = null,
//    var location: String? = null,
//    var country: String? = null,
//    var adminVerified: Boolean? = null
//){
//    fun setPassword(password: String) {
//        this.password = password
//    }
//}


package com.wfo_exception_tracker.userservice.dto

data class UserDto(
    var ibsEmpId: Long,
    var userName: String,
    var password: String,
    var role: String,
    var emailId: String,
    var phoneNumber: Long,
    var location: String,
    var country: String,
    var adminVerified: Boolean
) {
//    fun setPassword(password: String) {
//        this.password = password
//    }
}
