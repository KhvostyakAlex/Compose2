package ru.leroymerlin.internal.compose2.dataclass

import java.util.*

data class IntraruAuthResponse(val userHash: String,
                               val token:String,
                               val refreshToken: String,
                               val expiresIn:Int,
                               val expiresOn: Int
                            ):BaseCellModel



