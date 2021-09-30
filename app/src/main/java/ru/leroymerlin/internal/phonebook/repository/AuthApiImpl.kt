package ru.leroymerlin.internal.phonebook.repository

import androidx.lifecycle.viewModelScope
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthResponse
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.phonebook.di.AppModule

class AuthApiImpl: AuthApi {
    override fun getTokens(body: RequestBody): Single<IntraruAuthResponse> {

        TODO("Not yet implemented")

    }

    override fun refreshToken(refreshToken: RequestBody): Single<IntraruAuthResponse> {
        TODO("Not yet implemented")
    }
}