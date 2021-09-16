package ru.leroymerlin.internal.phonebook.dataclass

data class IntraruAuthUserList(val message: String,
                               val login:String,
                               val IntraruAuthUserData:IntraruAuthUserData
                            ):BaseCellModel

data class IntraruAuthUserData(val userHash: String,
                               val token:String,
                               val refreshToken: String,
                               val expiresIn:Int,
                               val expiresOn: Int)


