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
import androidx.compose.runtime.livedata.observeAsState
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
import androidx.lifecycle.Observer
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel

import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.di.AppModule
import ru.leroymerlin.internal.compose2.repository.PhoneBookRepository
import android.preference.PreferenceManager

import android.content.SharedPreferences
import android.text.method.TextKeyListener.clear
import ru.leroymerlin.internal.compose2.*
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList


@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController:NavController){
    val authData:List<IntraruAuthUserList> by loginViewModel.authData.observeAsState(emptyList())
    val userData:List<IntraruUserDataList> by loginViewModel.userData.observeAsState(emptyList())
    val error:String by loginViewModel.error.observeAsState("")
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val textStateLogin = remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }

  //  val authDat = loginViewModel.authDat.collectAsState()
    Scaffold() {



    Column(//verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,

        modifier = Modifier
            .fillMaxWidth()
            ) {
        Image(
            painter = painterResource(id = R.drawable.logo_leroy_merlin),
            contentDescription = "Profile pic",
            //contentScale = ContentScale.
        modifier = Modifier
           // .graphicsLayer{ scaleX = 3f; scaleY = 3f}
            .padding(top = 64.dp)
        )
        Text("Добро пожаловать!", modifier = Modifier.padding(8.dp))

        TextField(value = textStateLogin.value,
            onValueChange = { value -> textStateLogin.value = value},
            placeholder = {Text("LDAP")},
            trailingIcon = { if(textStateLogin.value.length ==8){ Text("V")} }, //при вводе 8 знаков появится иконка
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(50.dp)
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            placeholder = { Text("Введи пароль") },
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisibility)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                IconButton(onClick = {
                    passwordVisibility = !passwordVisibility
                }) {
                    Icon(imageVector  = image, "")
                }
            },
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .height(50.dp)
        )

        Button(onClick = {

            if(textStateLogin.value.isNotEmpty() && textStateLogin.value.length ==8){
                if(password.isNotEmpty()){
                   val  login = textStateLogin.value
                    //Log.e("onClick", "${textStateLogin.value} $password")


                      loginViewModel.authIntraru(login, password)

                  //  Log.e("authDat in button  - ", authData.toString())
                        hideKeyboard(activity)






                   // val item by LoginViewModel().authData
                    ///PhonebookList(navController, LoginViewModel(), textStateLogin.value, password)

                  //  Log.e("context as Activity - ",  (context as Activity).application.toString())
//" ru.leroymerlin.internal.phonebook.PhoneBookApplication@7cb0cf4"

                    /*loginViewModel.authIntraru(phoneBookApi = ((context as Activity).application as? PhoneBookApplication)?.phoneBookApi!!,
                        textStateLogin.value, password)

                     */
                  /* val result =  loginViewModel.authIntraru(textStateLogin.value, password)
                    Log.e("result - ",  result.toString())

                   */





                }else{
                    Toast.makeText( activity, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText( context, "Что-то не то с LDAP", Toast.LENGTH_SHORT).show()
            }
                         },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = Modifier.size(150.dp, 50.dp)) {
            Text("Войти", color = Color.White)
        }

    }


    }


    if (authData.isNotEmpty()) {
        val t = authData[0] as IntraruAuthUserList
        with (sharedPref.edit()) {
            putString("token", t.token)
            putString("refreshToken", t.refreshToken)
            putInt("expiresIn", t.expiresIn)
            putInt("expiresOn", t.expiresOn)
            apply()
        }
        val authHeader = "Bearer " + t.token
        loginViewModel.getInfoUser(textStateLogin.value, authHeader)


    }

    if (userData.isNotEmpty()) {

        val uData = userData[0] as IntraruUserDataList
        Log.e("uData", uData.toString())
        //  Log.e("Login fragment USER DATA mobilePhone - ", uData.workPhone)
        val today = getCalculatedDate("yyyy-M-dd", 0)
        with (sharedPref.edit()) {
            putString("account", uData.account)
            putString("firstName", uData.firstName)
            putString("lastName", uData.lastName)
            putString("shopNumber", uData.shopNumber)
            putString("cluster", uData.cluster)
            putString("region", uData.region)
            putString("jobTitle", uData.jobTitle)
            putString("department", uData.department)
            putString("subDivision", uData.subDivision)
            putString("workPhone", uData.workPhone)
            putString("mobilePhone", uData.mobilePhone)
            putString("personalEmail", uData.personalEmail)
            putString("today", today)
            putBoolean("signin?", true)
            apply()
        }

        //navController.navigate("search")

    }






    Log.e("authDat - ", authData.toString())
    Log.e("userData - ", userData.toString())
    Log.e("error - ", error.toString())


    val token = sharedPref.getString("token", "") //достаем данные из shared prefs
   // Log.e("sharePrefs", highScore.toString())

/*
    if(error.isNotEmpty()) {
        //  Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

 */
}
 fun test(activity: Activity){
    // val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
     val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
 }





