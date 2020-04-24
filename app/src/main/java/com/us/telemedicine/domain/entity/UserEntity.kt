package com.us.telemedicine.domain.entity


data class UserEntity(
    val firstName: String,
    val lastName: String,
    val email: String?,
    val phone: String?,
    val password: String?,
    val role: Role
)

enum class Role(val role: String){
    Patient("Patient"),
    Doctor("Doctor"),
    UltrasoundUser("UltrasoundUser"),
    Unknown("Unknown");
    companion object{
         fun valueOrNullFrom(constant: String?): Role? {
            return values().firstOrNull { e -> e.role == constant }
        }
    }
}