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
import androidx.lifecycle.ViewModel
import androidx.navigation.NavController
import ru.leroymerlin.internal.compose2.PhoneBookApi
import ru.leroymerlin.internal.compose2.PhoneBookApplication
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.di.AppModule
import ru.leroymerlin.internal.compose2.repository.PhoneBookRepository


@Composable
fun LoginScreen(navController:NavController, loginViewModel: LoginViewModel){

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
                    Log.e("onClick", "${textStateLogin.value} $password")
                    Log.e("application - ",  (activity.application as? PhoneBookApplication?).toString())
                    Log.e("application - ",  (activity.application).toString())
                    Log.e("context - ",  (context as Activity).toString())

                  //  Log.e("context as Activity - ",  (context as Activity).application.toString())
//" ru.leroymerlin.internal.phonebook.PhoneBookApplication@7cb0cf4"

                    /*loginViewModel.authIntraru(phoneBookApi = ((context as Activity).application as? PhoneBookApplication)?.phoneBookApi!!,
                        textStateLogin.value, password)

                     */






                }else{
                    Toast.makeText( context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
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
}



fun onClickLogin(context:Context, login:String, password:String){

    if(login.isNotEmpty() && login.length ==8){
        if(password.isNotEmpty()){
            Log.e("onClick", "$login $password")




    }else{
        Toast.makeText( context, "Неправильный логин", Toast.LENGTH_SHORT).show()
    }

    }

}