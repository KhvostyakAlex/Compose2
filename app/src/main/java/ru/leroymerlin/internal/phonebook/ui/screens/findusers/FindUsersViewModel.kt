package ru.leroymerlin.internal.phonebook.ui.screens.findusers

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.phonebook.dataclass.*
import ru.leroymerlin.internal.phonebook.di.AppModule
import ru.leroymerlin.internal.phonebook.di.AppModule.provideAuthApi
import ru.leroymerlin.internal.phonebook.di.AppModule.provideUserApi


class FindUsersViewModel: ViewModel() {
    private val _cards: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var cards: LiveData<List<IntraruUserDataList>> = _cards

    private val _expandedCardIdsList = MutableLiveData(listOf<Int>())
    val expandedCardIdsList: LiveData<List<Int>> get() = _expandedCardIdsList

    private val _userData: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var userData: LiveData<List<IntraruUserDataList>> = _userData

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error

    private val _depData = MutableLiveData<List<String>>(emptyList())
    var depData:LiveData<List<String>> = _depData

    private val _tokenData: MutableLiveData<MutableList<IntraruAuthUserData>> = MutableLiveData()
    var tokenData: LiveData<MutableList<IntraruAuthUserData>> = _tokenData

    fun getInfoUser(ldap: String, authHeader:String) {
        // Log.e("LOG mess", "response ldap "+ldap.toString())
        viewModelScope.launch(Dispatchers.Default) {
           // AppModule.providePhonebookApi().getUser(ldap, authHeader)//здесь вызывается API
            provideUserApi().getUser(ldap, authHeader)//здесь вызывается API
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val status = response?.userStatus?.status
                    val testData = ArrayList<IntraruUserDataList>()
                    Log.e("response", response.toString())
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
                        testData
                    }
                    _cards.postValue(testData)
                }, {
                    _error.postValue("Er - ${it.localizedMessage}")
                    val testData = ArrayList<IntraruUserDataList>()
                    _cards.postValue(testData)
                })
        }
    }


    fun getUserByName(userName: String, authHeader:String) {
        // Log.e("LOG mess", "userName - "+userName.toString())
        viewModelScope.launch(Dispatchers.Default) {
            provideUserApi()
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
                    _cards.postValue(testData)
                }, {
                    _error.postValue("Er - ${it.localizedMessage}")
                    //_error.postValue("Ничего не найдено")
                    val testData = ArrayList<IntraruUserDataList>()
                    _cards.postValue(testData)
                })


        }
    }

    fun getDepartment( authHeader:String) {
        viewModelScope.launch(Dispatchers.Default) {
            provideUserApi().getDepartment(authHeader)//здесь вызывается API
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({ response ->
                    val testData = ArrayList<String>()
                    val data = (response as DepartmentList).hits
                    //  Log.e("FindDepartment ", "dep - "+data.toString())
                    for (row in data) {
                        val orgUnitName = row.orgUnitCard.name
                        testData.add(orgUnitName)
                    }

                    _depData.postValue(testData)
                }, {
                    Log.e("error - ", "er "+ it.localizedMessage.toString())
                    _error.postValue("Er - ${it.localizedMessage}")
                    //_error.postValue("Ничего не найдено")
                })
        }
    }

    fun refreshToken(refreshToken:String){
        // Log.e("authIntraru", "authIntraru")
        viewModelScope.launch(Dispatchers.Default) {
            val body = JSONObject()
            body.put("refreshToken", refreshToken)
            val testData = ArrayList<IntraruAuthUserData>()

            provideAuthApi()
                .refreshToken(body.toString().toRequestBody("body".toMediaTypeOrNull()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({response ->
                    Log.e("refreshToken", "Success")
                    testData.add( IntraruAuthUserData(

                            response.userHash,
                            response.token,
                            response.refreshToken,
                            response.expiresIn,
                            response.expiresOn)

                    )
                    _tokenData.postValue(testData)

                }, {


                })
        }
    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value?.toMutableList().also { list ->
            if (list!!.contains(cardId)) list.remove(cardId) else list?.add(cardId)
        }
    }
}
