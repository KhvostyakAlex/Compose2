package ru.leroymerlin.internal.compose2.ui.screens

import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontSynthesis.Companion.All
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.core.content.res.ResourcesCompat
import androidx.navigation.NavController
import ru.leroymerlin.internal.compose2.R


@Composable
fun LoginScreen(navController:NavController){
    Scaffold() {
        val textStateLogin = remember { mutableStateOf("") }
        val textStatePassword = remember { mutableStateOf("") }
//Icon(painter = painterResource(R.drawable.abc_vector_test), contentDescription = "logo")
       // val imageLogo: Painter = imageResource(id = R.drawable.image2vector)

/*
        Image(
            (ResourcesCompat.getDrawable(Resources, R.drawable.logo_mid, null) as BitmapDrawable).bitmap
        )*/

                    // Image(bitmap = , contentDescription = )


        var password by rememberSaveable { mutableStateOf("") }
        var passwordVisibility by remember { mutableStateOf(false) }


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

        Button(onClick = {},
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
        modifier = Modifier.size(150.dp, 50.dp)) {
            Text("Войти", color = Color.White)
        }

    }


     /*   Card(elevation = 8.dp,
        backgroundColor = Color.White,
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(horizontalArrangement = Arrangement.SpaceBetween) {
                //CardTitle(title = card.title)
                Text("Hello")
                Text("Hello")

                // CardArrow(degrees = arrowRotationDegree, onClick = onCardArrowClick)

            }
        }*/

    }
}