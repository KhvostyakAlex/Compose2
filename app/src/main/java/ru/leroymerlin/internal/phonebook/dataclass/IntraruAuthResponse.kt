package ru.leroymerlin.internal.phonebook.dataclass

data class IntraruAuthResponse(val userHash: String,
                               val token:String,
                               val refreshToken: String,
                               val expiresIn:Int,
                               val expiresOn: Int
                            ):BaseCellModel



