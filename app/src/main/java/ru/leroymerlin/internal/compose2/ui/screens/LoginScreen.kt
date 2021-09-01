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
import ru.leroymerlin.internal.compose2.PhoneBookApi

import ru.leroymerlin.internal.compose2.PhoneBookApplication
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.di.AppModule
import ru.leroymerlin.internal.compose2.hideKeyboard
import ru.leroymerlin.internal.compose2.repository.PhoneBookRepository


@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController:NavController){
    val authData:List<IntraruAuthUserList> by loginViewModel.authData.observeAsState(emptyList())
    val error:String by loginViewModel.error.observeAsState("")
    val context = LocalContext.current

  //  val authDat = loginViewModel.authDat.collectAsState()
    Scaffold() {
        val textStateLogin = remember { mutableStateOf("") }
        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }
        val context = LocalContext.current
        val activity = LocalContext.current as Activity

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
                    val login = textStateLogin.value
                    //Log.e("onClick", "${textStateLogin.value} $password")


                      loginViewModel.authIntraru(login, password)

                    Log.e("authDat in button  - ", authData.toString())
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

    Log.e("authDat - ", authData.toString())
    Log.e("error - ", error.toString())

/*
    if(error.isNotEmpty()) {
        //  Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

 */
}






