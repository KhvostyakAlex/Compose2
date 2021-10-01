package ru.leroymerlin.internal.phonebook.ui.screens.login


import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.phonebook.di.AppModule.provideAuthApi
import ru.leroymerlin.internal.phonebook.di.AppModule.provideUserApi


class LoginViewModel: ViewModel() {

    private val _authData: MutableLiveData<MutableList<IntraruAuthUserList>> = MutableLiveData()
    var authData: LiveData<MutableList<IntraruAuthUserList>> = _authData

    private val _userData: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var userData: LiveData<List<IntraruUserDataList>> = _userData

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error

    //private val _tickFlow = MutableSharedFlow<List<IntraruAuthUserList>>(replay = 0)
   // val tickFlow: SharedFlow<List<IntraruAuthUserList>> = _tickFlow

    fun authIntraru(login:String, pass:String){
       // Log.e("authIntraru", "authIntraru")
        viewModelScope.launch(Dispatchers.Default) {
            val body = JSONObject()
            body.put("login", "RU1000\\$login")
            body.put("password", pass)
            //AuthApiImpl().getTokens(body.toString().toRequestBody("body".toMediaTypeOrNull()))

            val testData = ArrayList<IntraruAuthUserList>()
              provideAuthApi()
                    .getTokens(body.toString().toRequestBody("body".toMediaTypeOrNull()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({response ->
                       // Log.e("VM response", response.toString())
                        testData.add( IntraruAuthUserList(
                            "Success",
                            login = login,
                            IntraruAuthUserData(
                                response.userHash,
                                response.token,
                                response.refreshToken,
                                response.expiresIn,
                                response.expiresOn)
                        ))
                        _authData.postValue(testData)
                        //_tickFlow.tryEmit(testData)

                    }, {

                        testData.add(
                            IntraruAuthUserList(
                            "Failed",
                                login = login,
                            IntraruAuthUserData(
                              "","","",0,0)
                            )
                        )
                       // Log.e("VM login", "empty testData - $testData")
                        _authData.postValue(testData)
                        // _error.postValue("Er - ${it.localizedMessage}")
                       // _error.postValue("Неверный логин или пароль")
                    })
              }
        }

    fun getInfoUser(ldap: String, authHeader:String) {
    //    Log.e("VM ldap", ldap.toString())
      //  Log.e("VM authHeader", authHeader.toString())
        viewModelScope.launch(Dispatchers.Default) {
            provideUserApi().getUser(ldap, authHeader)//здесь вызывается API
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({response ->
                        val testData = ArrayList<IntraruUserDataList>()
                        testData.add(IntraruUserDataList(
                            response?.common!!.account,
                            response?.common!!.firstName.toString(),
                            response?.common!!.lastName.toString(),
                            response?.common!!.orgUnitName.toString(),
                            response?.common!!.shopNumber.toString(),
                            response?.common!!.cluster.toString(),
                            response?.common!!.region.toString(),
                            response?.common!!.jobTitle.toString(),
                            response?.common!!.department.toString(),
                            response?.common!!.subDivision.toString(),
                            response?.contacts!!.workPhone?.value.toString(),
                            response?.contacts!!.mobilePhone?.value.toString(),
                            response?.contacts!!.personalEmail?.value.toString(),
                            response?.contacts!!.workEmails?.toString(),

                            ))
                        _userData.postValue(testData)
                    }, {
                        _error.postValue("Ошибка - ${it.localizedMessage}")
                    })

        }
    }


}



