package ru.leroymerlin.internal.compose2.ui.screens


import android.os.Parcel
import android.os.Parcelable
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.compose2.PhoneBookApi
import ru.leroymerlin.internal.compose2.dataclass.BaseCellModel
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.di.AppModule
import javax.inject.Inject


class LoginViewModel @Inject constructor(
  //  private val repository: PhoneBookRepository
): ViewModel() {
    private val compositeDisposable: CompositeDisposable = CompositeDisposable()
var authData = mutableStateOf<List<BaseCellModel>>(listOf())
    private val _authData = MutableLiveData<List<BaseCellModel>>(emptyList())

    private val _error = MutableLiveData<String>("")
    var error: LiveData<String> =_error


    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)
    var endReached = mutableStateOf(false)


    fun authIntraru(login:String, pass:String){
        Log.e("testFun", "-treu")
      //  repository.authIntraru(login, pass)
        viewModelScope.launch {
            val body = JSONObject()
            body.put("login", "RU1000\\$login")
            body.put("password", pass)


                AppModule.providePhonebookApi()
                    .getUserInt(body.toString().toRequestBody("body".toMediaTypeOrNull()))
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({response ->
                        val testData = ArrayList<BaseCellModel>()
                        Log.e("response - ", response.toString())
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






              }
        }
/*
    fun authIntraru(login:String, pass:String){
        viewModelScope.launch {
            isLoading.value = true
            val result = repository.authIntraru(login, pass)
            when(result){
                is Resource.Success ->{
                    result.data

                }
                is Resource.Error -> {

                }
            }
        }
    }

 */

}


