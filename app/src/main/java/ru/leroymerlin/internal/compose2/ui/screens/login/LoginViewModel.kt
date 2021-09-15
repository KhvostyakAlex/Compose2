package ru.leroymerlin.internal.compose2.ui.screens.login


import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserData
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.di.AppModule


class LoginViewModel: ViewModel() {

    private val _authData: MutableLiveData<MutableList<IntraruAuthUserList>> = MutableLiveData()
    var authData: LiveData<MutableList<IntraruAuthUserList>> = _authData

    private val _userData: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var userData: LiveData<List<IntraruUserDataList>> = _userData

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error

    fun authIntraru(login:String, pass:String){
       // Log.e("authIntraru", "authIntraru")
        viewModelScope.launch(Dispatchers.Default) {
            val body = JSONObject()
            body.put("login", "RU1000\\$login")
            body.put("password", pass)
            val testData = ArrayList<IntraruAuthUserList>()

              AppModule.providePhonebookApi()
                    .getUserInt(body.toString().toRequestBody("body".toMediaTypeOrNull()))
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

                    }, {

                        testData.add(
                            IntraruAuthUserList(
                            "Failed",
                                login = login,
                            IntraruAuthUserData(
                              "","","",0,0)
                            )
                        )
                        Log.e("VM login", "empty testData - $testData")
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
            AppModule.providePhonebookApi().getUser(ldap, authHeader)//здесь вызывается API
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



