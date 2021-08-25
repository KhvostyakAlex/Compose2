package ru.leroymerlin.internal.compose2.ui.screens

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import ru.leroymerlin.internal.compose2.PhoneBookApi
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.BaseCellModel
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList

private val compositeDisposable: CompositeDisposable = CompositeDisposable()
private val _authData = MutableLiveData<List<BaseCellModel>>(emptyList())

var authData: LiveData<List<BaseCellModel>> = _authData
private val _error = MutableLiveData<String>("")
var error:LiveData<String> =_error

@Composable
fun LoginFun(){
    fun authIntraru(phoneBookApi: PhoneBookApi, login:String, pass:String) {

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
}