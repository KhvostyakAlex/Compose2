package ru.leroymerlin.internal.phonebook.ui.screens.search

import android.util.Log
import androidx.lifecycle.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.phonebook.dataclass.*
import ru.leroymerlin.internal.phonebook.di.AppModule


class SearchViewModel: ViewModel() {
    private val _cards: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var cards: LiveData<List<IntraruUserDataList>> = _cards

    private val _expandedCardIdsList = MutableLiveData(listOf<Int>())
    val expandedCardIdsList: LiveData<List<Int>> get() = _expandedCardIdsList

    private val _userData: MutableLiveData<List<IntraruUserDataList>> = MutableLiveData()
    var userData: LiveData<List<IntraruUserDataList>> = _userData

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error

    private val _connect = MutableLiveData<List<String>>(emptyList())
    var connect:LiveData<List<String>> = _connect

    private val _tokenData: MutableLiveData<MutableList<IntraruAuthUserList>> = MutableLiveData()
    var tokenData: LiveData<MutableList<IntraruAuthUserList>> = _tokenData

    fun refreshToken(refreshToken:String){
         Log.e("API refreshToken ", "refrToken - "+refreshToken)
        viewModelScope.launch(Dispatchers.Default) {
            val body = JSONObject()
            body.put("refreshToken", refreshToken)
            val testData = ArrayList<IntraruAuthUserList>()

            AppModule.providePhonebookApi()
                .refreshToken(body.toString().toRequestBody("body".toMediaTypeOrNull()))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({response ->
                    //Log.e("refreshToken", "Success")
                    testData.add( IntraruAuthUserList(
                        "Success","",
                        IntraruAuthUserData(
                            response.userHash,
                            response.token,
                            response.refreshToken,
                            response.expiresIn,
                            response.expiresOn)

                    ))

                   // val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
                    _tokenData.postValue(testData)

                }, {
                  /*  testData.add( IntraruAuthUserList(
                        "Failed","",
                        IntraruAuthUserData(
                            "",
                            "",
                            "",
                            0,
                            0)
                    ))
                    _tokenData.postValue(testData)

                   */
                })
        }
    }

    fun getConnect( authHeader:String) {
        viewModelScope.launch(Dispatchers.Default) {
            AppModule.providePhonebookApi().getDepartment(authHeader)//здесь вызывается API
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
                    _connect.postValue(testData)

                }, {
                   // _error.postValue("Er - ${it.localizedMessage}")
                    //_error.postValue("Ничего не найдено")
                    val testData = ArrayList<String>()
                    _connect.postValue(testData)
                })
        }
    }

    fun onCardArrowClicked(cardId: Int) {
        _expandedCardIdsList.value = _expandedCardIdsList.value?.toMutableList().also { list ->
            if (list!!.contains(cardId)) list.remove(cardId) else list?.add(cardId)
        }
    }
}
