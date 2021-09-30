package ru.leroymerlin.internal.phonebook.repository

import io.reactivex.Single
import okhttp3.RequestBody
import retrofit2.http.*
import ru.leroymerlin.internal.phonebook.dataclass.*



interface AuthApi {

    @POST("https://intraru3.leroymerlin.ru/services/identity/api/Identity/Login")
    @Headers("Content-Type: application/json")
    fun getTokens(@Body body: RequestBody): Single<IntraruAuthResponse>

    @POST("https://intraru3.leroymerlin.ru/services/identity/api/Identity/RefreshToken")
    @Headers("Content-Type: application/json")
    fun refreshToken(@Body refreshToken: RequestBody): Single<IntraruAuthResponse>

}

