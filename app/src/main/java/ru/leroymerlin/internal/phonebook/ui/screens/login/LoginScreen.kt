package ru.leroymerlin.internal.phonebook.ui.screens.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import ru.leroymerlin.internal.phonebook.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.phonebook.*
import ru.leroymerlin.internal.phonebook.R
import ru.leroymerlin.internal.phonebook.dataclass.IntraruUserDataList

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun LoginScreen(loginViewModel: LoginViewModel, navController:NavController){
    val authData:List<IntraruAuthUserList> by loginViewModel.authData.observeAsState(emptyList())
    //val userData:List<IntraruUserDataList> by loginViewModel.userData.observeAsState(emptyList())
    val error:String by loginViewModel.error.observeAsState("")
    val context = LocalContext.current
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    val textStateLogin = remember { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisibility by remember { mutableStateOf(false) }
    val signin = sharedPref.getBoolean("signin?", false) //достаем данные из shared prefs
    val focusManager = LocalFocusManager.current

    val keyboardController = LocalSoftwareKeyboardController.current

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

        TextField(
            value = textStateLogin.value,
            onValueChange = { value -> textStateLogin.value = value},
            placeholder = {Text("LDAP")},
            singleLine = true,
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.colorLightGrey),
                focusedIndicatorColor =  Color.Transparent ,                  //Color.Transparent - hide the indicator
                   unfocusedIndicatorColor = Color.Transparent
            ),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Next),
           //настраиваем кнопку ДАЛЕЕ, переходим на поле нижу
            keyboardActions = KeyboardActions(
                onNext = {
                    focusManager.moveFocus(FocusDirection.Down)
                }
            ),
            trailingIcon = { if(textStateLogin.value.length ==8){
                                    Icon(imageVector  = Icons.Filled.Check, "")
                                }
                           }, //при вводе 8 знаков появится иконка
            modifier = Modifier
                .padding(8.dp)
                .width(200.dp)
                .onKeyEvent {
                    if (it.key.keyCode == Key.Tab.keyCode) {
                        focusManager.moveFocus(FocusDirection.Down)
                        true
                    } else {
                        false
                    }
                },
        )

        TextField(
            value = password,
            onValueChange = { password = it },
            singleLine = true,
            placeholder = { Text("Введи пароль") },
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = colorResource(id = R.color.colorLightGrey),
                //focusedIndicatorColor =  colorResource(id = R.color.lmNCKD),                   //Color.Transparent - hide the indicator
                focusedIndicatorColor =  Color.Transparent,                   //Color.Transparent - hide the indicator
                   unfocusedIndicatorColor = Color.Transparent
            ),
            visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password, imeAction = ImeAction.Send),
            keyboardActions = KeyboardActions(
                onSend = {
                    if(textStateLogin.value.isNotEmpty() && textStateLogin.value.length ==8){
                        if(password.isNotEmpty()){
                            val  login = textStateLogin.value.trim()
                            loginViewModel.authIntraru(login, password)
                            keyboardController?.hide()
                          //  password =""
                        }else{
                            Toast.makeText( context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                        }
                    }else{
                        Toast.makeText( context, "Что-то не то с LDAP", Toast.LENGTH_SHORT).show()
                    }
                    keyboardController?.hide()}
            ),
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
                //.height(50.dp)
        )

        Button(onClick = {
            if(textStateLogin.value.isNotEmpty() && textStateLogin.value.length ==8){
                if(password.isNotEmpty()){
                    val  login = textStateLogin.value.trim()
                    loginViewModel.authIntraru(login, password)
                    keyboardController?.hide()

                }else{
                    Toast.makeText( context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                }
            }else{
                Toast.makeText( context, "Что-то не то с LDAP", Toast.LENGTH_SHORT).show()
            }
                         },
            shape = RoundedCornerShape(8.dp),
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier.size(150.dp, 50.dp)) { Text("Войти", color = Color.White) }
    }
    }
        //при запуске
        LaunchedEffect(Unit){
            if(signin){
                loginViewModel.authIntraru("login", "password")
                navController.navigate("search"){
                    popUpTo("search") {
                        inclusive = true
                    }
                }
            }
        }
    //при покиданиии
        DisposableEffect(Unit ){
            onDispose {
                /*костыль, при повторном заходе на страницу Логин, liveData еще жива и
                 срабатывает скрипт повторного захода в приложение. Нужно реализовать отписку
                 от liveData когда уходим со страницы Логин*/
                loginViewModel.authIntraru("login", "password")
            }
        }

   /* for(row in authData){
        val r = row as IntraruAuthUserList
        Log.e("row -", r.token.toString())
        val authData = ""
    }*/

    if (authData.isNotEmpty()) {
        if(authData[0].message == "Success"){
            SignIn(
                login = textStateLogin.value,
                password = password,
                authData = authData[0],
                navController = navController
            )
        }else if(authData[0].message == "Failed" && authData[0].login != "login"){
            /*костыль, потому что при первом вводе ложного пароля Тост показывается,
            при повторном вводе кривого пароля authData не обновляется и уже не показываетсяю
            необходимо реализовать после каждого нажатия Button-> обновлять LiveData */
            Toast.makeText(context, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
            loginViewModel.authIntraru("login", "password")
        }
    }
}

@Composable
fun SignIn(login:String,
             password:String,
             authData:IntraruAuthUserList,
             loginViewModel: LoginViewModel = LoginViewModel(),
             navController: NavController
){
    val userData:List<IntraruUserDataList> by loginViewModel.userData.observeAsState(emptyList())
    val t = authData.IntraruAuthUserData
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    Log.e("authData - ", t.toString())
    //authData -: IntraruAuthUserData(userHash=9f6aa352d516d11fd9cd49e03d858437e5d4766455183675499d82189ad1a113, token=eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9uYW1lIjoiNjAwMzIyNDYiLCJodHRwOi8vc2NoZW1hcy54bWxzb2FwLm9yZy93cy8yMDA1LzA1L2lkZW50aXR5L2NsYWltcy9naXZlbm5hbWUiOiLQkNC70LXQutGB0LXQuSDQpdCy0L7RgdGC0Y_QuiIsImh0dHA6Ly9zY2hlbWFzLnhtbHNvYXAub3JnL3dzLzIwMDUvMDUvaWRlbnRpdHkvY2xhaW1zL2VtYWlsYWRkcmVzcyI6IjYwMDMyMjQ2QGxlcm95bWVybGluLnJ1IiwiaHR0cDovL3NjaGVtYXMueG1sc29hcC5vcmcvd3MvMjAwNS8wNS9pZGVudGl0eS9jbGFpbXMvdXBuIjoiNjAwMzIyNDYiLCJodHRwOi8vc2NoZW1hcy5taWNyb3NvZnQuY29tL3dzLzIwMDgvMDYvaWRlbnRpdHkvY2xhaW1zL3JvbGUiOlsiTE0tU0MtU00tSVRNYWciLCJBTEwtTE1SVSIsIkxNUlVfQWxsIiwiTE0tU1AtSW5mcmEtRG9jcy1TZXJ2aWNlcy1WaWV3IiwiTE0tU2hhcmUtTE1fTWVkaWEtUmVhZE9ubHkiLCJEb21haW4gVXNlcnMiLCJMTS1SRFMtVFMwMC1TaG9ydEN1dCIsIkRpdmlzaW9uLTg4MCIsIkRlcGFydG1lbnQtNzE1IiwiTE0tRHluYXRyYWNlLXVzZXJzIiwiSVRfRGl2aXNpb243X1NlYyIsIk9mZmljZTM2NV9GMSIsIlNob3AtMDM2LWFsbFBlcnNvbmFsIiwiQUxMLUxNUlUtRW1wIiwiTE0tVm9JUC1DaXNjb0NVQ00iLCJJVF9EaXZpc2lvbjFfU2VjIiwiSVRfRGl2aXNpb243IiwiSW50ZXJuZXRXV1dVc2VycyIsIkxNUlUgTW9iaWxpdHkgVXNlcnMiLCJBbGxfTGRhcCIsIlNob3AtMDM2LWFsbCIsIkFMTC1NYWctVGl0bGUtNTMtU3BlY1BvSVQiXSwibW9kdWxlIjoiQWxsIiwibmJmIjoxNjMxNzIwMDM2LCJleHAiOjE2MzE3MjM2MzYsImlzcyI6Imh0dHA6Ly9hZGZzLmxlcm95bWVybGluLnJ1L2FkZnMvc2VydmljZXMvdHJ1c3QiLCJhdWQiOiJ1cm46a3ViZXJwcm9kOmFkZnMifQ.HH6xAx12zzA0o1q26U3xLpfkWYLcbeZLYXmJeR_i8Zg, refreshToken=170922a0569d6f69629b8a753b5e8c516798d8746c227af9d2dd1715b95f4cb8, expiresIn=1631720036, expiresOn=1631748836)

    with (sharedPref.edit()) {
        putString("token", t.token)
        putString("refreshToken", t.refreshToken)
        putInt("expiresIn", t.expiresIn)
        putInt("expiresOn", t.expiresOn)
        putString("authHeader", "Bearer " + t.token)
        apply()
    }

    val authHeader = "Bearer " + t.token
    if(login.isNotEmpty() && password.isNotEmpty()){
       // loginViewModel.authIntraru(login, password)
        loginViewModel.getInfoUser(login, authHeader)
    }

    if (userData.isNotEmpty()) {
        val uData = userData[0] as IntraruUserDataList
        val today = getCalculatedDate("yyyy-M-dd", 0)
        with (sharedPref.edit()) {
//Log.e("Udata - ", uData.toString())
           // IntraruUserDataList(account=60032246, firstName=Алексей, lastName=Хвостяк, orgUnitName=Магазин Барнаул 1, shopNumber=036, cluster=Барнаул, region=Регион 7, jobTitle=специалист технической поддержки, department=Магазин Барнаул 1, subDivision=центр обслуживания магазинов, workPhone=+7 909 932-94-01, mobilePhone=null, personalEmail=null, workEmail=[{value=Aleksey.Hvostyak@leroymerlin.ru, isConfirmed=true, confrimCodeInfo=null}], expandable=false)
            putString("account", uData.account)
            putString("firstName", uData.firstName)
            putString("lastName", uData.lastName)
            putString("orgUnitName", uData.orgUnitName)
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

        navController.navigate("search") {
            launchSingleTop = true //переходим только 1 раз
        }
    }
}





