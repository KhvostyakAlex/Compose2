package ru.leroymerlin.internal.compose2.ui.screens.findusers


import android.annotation.SuppressLint
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
import ru.leroymerlin.internal.compose2.dataclass.BaseCellModel
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserByNameList
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.di.AppModule


class FindUsersViewModel: ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
/*
    private val _authDat = MutableStateFlow(listOf<IntraruAuthUserList>())
    val authDat: StateFlow<List<IntraruAuthUserList>> get() = _authDat*/




    override fun onCleared() {
        compositeDisposable.dispose()
        super.onCleared()
    }

    private val _userData: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var userData: LiveData<List<IntraruUserDataList>> = _userData

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error

    fun getInfoUser(ldap: String, authHeader:String) {
        // Log.e("LOG mess", "response ldap "+ldap.toString())

        viewModelScope.launch(Dispatchers.Default) {
            AppModule.providePhonebookApi().getUser(ldap, authHeader)//здесь вызывается API
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ response ->
                        val status = response?.userStatus?.status
                        val testData = ArrayList<IntraruUserDataList>()
                        //добавляем в список всех работающих
                        if(status!="fired"){
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
                                response?.contacts!!.workEmails.toString(),
                                ))
                        }
                        //если ничего не нашлось
                        if(testData.isEmpty()){
                            testData.add(IntraruUserDataList(
                                "null",
                                "Ничего не найдено",
                                "",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                ))
                        }
                        _userData.postValue(testData)
                    }, {
                        _error.postValue("Er - ${it.localizedMessage}")
                    })
        }
    }


    fun getUserByName(userName: String, authHeader:String) {
        // Log.e("LOG mess", "userName - "+userName.toString())
        viewModelScope.launch(Dispatchers.Default) {
            AppModule.providePhonebookApi()
                .getUserByName(userName, authHeader)//здесь вызывается API
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val testData = ArrayList<IntraruUserDataList>()
                    val data = (response as IntraruUserByNameList).hits
                    // Log.e("LOG mess", "response mess "+(response as IntraruUserByNameList).toString())
                    // Log.e("LOG mess", "response mess 22 "+(response as IntraruUserByNameList).hits)


                    for (row in data) {

                        //  Log.e("LOG mess", "row - "+ row.profile.userStatus.status)
                        //   testData.add(IntraruArrLdapList(row.id))
                        val status = row.profile.userStatus.status
                        //добавляем в список всех работающих
                        if (status != "fired") {
                            val common = row.profile.common
                            val contacts = row.profile.contacts

                            testData.add(
                                IntraruUserDataList(
                                    common.account,
                                    common.firstName.toString(),
                                    common.lastName.toString(),
                                    common.orgUnitName.toString(),
                                    common.shopNumber.toString(),
                                    common.cluster.toString(),
                                    common.region.toString(),
                                    common.jobTitle.toString(),
                                    common.department.toString(),
                                    common.subDivision.toString(),
                                    contacts!!.workPhone?.value.toString(),
                                    contacts!!.mobilePhone?.value.toString(),
                                    contacts!!.personalEmail?.value.toString(),
                                    contacts!!.workEmails.toString(),
                                    )
                            )
                        }
                    }
                    if (testData.isEmpty()) {
                        testData.add(
                            IntraruUserDataList(
                                "null",
                                "Ничего не найдено",
                                "",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",
                                "null",

                                )
                        )
                    }
                    _userData.postValue(testData)
                }, {
                    _error.postValue("Er - ${it.localizedMessage}")
                    //_error.postValue("Ничего не найдено")
                })


        }
    }


}



