package ru.leroymerlin.internal.compose2.ui.screens

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.leroymerlin.internal.compose2.PhoneBookApi
import ru.leroymerlin.internal.compose2.dataclass.*


class LoginViewModel_old : ViewModel(), Callback<UserIntraru> {

    private val _text = MutableLiveData<String>().apply {
        value = "Данный модуль в разработке"
    }

    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
    private val _error = MutableLiveData<String>("")
    var error:LiveData<String> =_error

    private var _status = MutableLiveData<String>()
            .apply { value = "false" }

    private var _lastTask = MutableLiveData<Int>()
            .apply { value = 0 }

    var status: LiveData<String> = _status
    var lastTask: LiveData<Int> = _lastTask



    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }


    val text: LiveData<String> = _text

    private val _authData = MutableLiveData<List<BaseCellModel>>(emptyList())

    var authData:LiveData<List<BaseCellModel>> = _authData

    private val _userData = MutableLiveData<List<BaseCellModel>>(emptyList())

    var userData:LiveData<List<BaseCellModel>> = _userData


    fun authIntraru(phoneBookApi: PhoneBookApi, login:String, pass:String) {
        Log.e("LoginViewModel", "3794659736479")
        val body = JSONObject()
        body.put("login", "RU1000\\$login")
        body.put("password", pass)
        phoneBookApi?.let{
            compositeDisposable.add(

                phoneBookApi.getUserInt(
                    body.toString().toRequestBody("body".toMediaTypeOrNull())
                )//здесь вызывается API
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({response ->
                        val testData = ArrayList<BaseCellModel>()
                        testData.add(IntraruAuthUserList(response.userHash,
                            response.token,
                            response.refreshToken,
                            response.expiresIn,
                            response.expiresOn))
                         _authData.postValue(testData)
                    }, {

                        // _error.postValue("Er - ${it.localizedMessage}")
                        _error.postValue("Неверный логин или пароль")
                    })


            )
        }
    }

    fun getInfoUser(myApi: PhoneBookApi, ldap: String, authHeader:String) {
        myApi?.let{
            compositeDisposable.add(
                myApi.getUser(ldap, authHeader)//здесь вызывается API
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({response ->
                        val testData = ArrayList<BaseCellModel>()
                        /*Log.e("LOG mess", "response mess "+response.toString())
                        Log.e("LOG mess", "response mess "+response?.account.toString())
                        Log.e("LOG mess", "response mess "+response?.common?.firstName)
                        
                         */

                        testData.add(IntraruUserDataList(
                            response?.common!!.account.toString(),
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

                        )
                        )
                          _userData.postValue(testData)
                    }, {
                        _error.postValue("Ошибка - ${it.localizedMessage}")
                    })
            )
        }
    }

    override fun onResponse(call: Call<UserIntraru>, response: Response<UserIntraru>) {

    }

    override fun onFailure(call: Call<UserIntraru>, t: Throwable) {

    }

}


