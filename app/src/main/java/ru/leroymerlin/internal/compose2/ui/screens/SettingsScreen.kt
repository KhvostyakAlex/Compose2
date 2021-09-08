package ru.leroymerlin.internal.compose2.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.rememberPagerState
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.TabItem
import ru.leroymerlin.internal.compose2.hideKeyboard


@ExperimentalPagerApi
@ExperimentalMaterialApi
@Composable
fun SettingsScreen() {


    Scaffold() {


        Column(//verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,

            modifier = Modifier
                .padding(8.dp)
        ) {


Row(horizontalArrangement = Arrangement.Start){
    ExitButton()
}
            Row(
               modifier = Modifier
                   .fillMaxWidth(),
               //verticalAlignment = Alignment.CenterVertically
           horizontalArrangement = Arrangement.SpaceAround
           ){
               Text("Имя")
               Text("Алексей")
           }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //verticalAlignment = Alignment.CenterVertically
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text("№ магазина")
                Text("036")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //verticalAlignment = Alignment.CenterVertically
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text("Должность")
                Text("036")
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                //verticalAlignment = Alignment.CenterVertically
                horizontalArrangement = Arrangement.SpaceAround
            ){
                Text("Телефон")
                Text("")
            }
        }
    }
}

@Composable
fun ExitBtn() {
    ExtendedFloatingActionButton(
        icon = { Icon(Icons.Filled.ExitToApp,"") },
        text = { Text("Выход") },
        onClick = { /*do something*/ },
        elevation = FloatingActionButtonDefaults.elevation(8.dp)
    )
}


@Composable
fun ExitButton(){
    val onClick = { /* Do something */ }

//Simple FAB
    FloatingActionButton(onClick = onClick,
    backgroundColor = colorResource(id = R.color.lmNCKD)) {
        Icon(Icons.Filled.ExitToApp,"")
    }
}

