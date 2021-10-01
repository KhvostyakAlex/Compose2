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

class AuthRepository: AuthApi {
    override fun getTokens(body: RequestBody): Single<IntraruAuthResponse> {

        TODO("Not yet implemented")
        /*
        * на данный момент во viewModel создается запрос и ответ содержит
        * IntraruAuthResponse. Далее в IntraruAuthResponse добавляю свои данные и
        * получаю IntraruAuthUserList, на полученный результат я подписываюсь
        * в LoginScreen.
        *Теперь:
        * По логике во ViewModel  должен отдавать уже готовый результат,
        * то есть IntraruAuthUserList (или нет?),
        * но тут получается только- "Single<IntraruAuthResponse>".
        * Если все таки должен отдавать Single<IntraruAuthResponse>,
        * то получается на IntraruAuthResponse я должен подписываться во ViewModel,
        * добавлять свои данные(получаю IntraruAuthUserList) и отправлять этот результат
        * в LoginScreen?
        * */

    }

    override fun refreshToken(refreshToken: RequestBody): Single<IntraruAuthResponse> {
        TODO("Not yet implemented")
    }
}