package ru.leroymerlin.internal.compose2.ui.screens.login

import android.app.Activity
import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import ru.leroymerlin.internal.compose2.dataclass.IntraruAuthUserList
import ru.leroymerlin.internal.compose2.*
import ru.leroymerlin.internal.compose2.R
import ru.leroymerlin.internal.compose2.dataclass.IntraruUserDataList
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginViewModel


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
                         onCLickButtonSignin(login = textStateLogin.value, password = password, activity =activity, navController =navController )
                           /* Log.e("onClick SignIn", "Login screen")
                                        if(textStateLogin.value.isNotEmpty() && textStateLogin.value.length ==8){
                                            if(password.isNotEmpty()){
                                                val  login = textStateLogin.value
                                                loginViewModel.authIntraru(login, password)
                                               // authIntraru( loginViewModel, login, password)
                                                hideKeyboard(activity)
                                            }else{
                                                Toast.makeText( activity, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
                                            }
                                        }else{
                                            Toast.makeText( context, "Что-то не то с LDAP", Toast.LENGTH_SHORT).show()
                                        }*/
                         },
            colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.primary),
            modifier = Modifier.size(150.dp, 50.dp)) { Text("Войти", color = Color.White) }
    }
    }








    //как только появляются данные, то записываем их в ref и переходим в поиск
    val lastRoute =navController.previousBackStackEntry?.destination?.route
    val token = sharedPref.getString("token", "") //достаем данные из shared prefs
   // Log.e("sharePrefs", highScore.toString())

/*
    if(error.isNotEmpty()) {
        //  Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

 */
}
 fun authIntraru(loginViewModel: LoginViewModel = LoginViewModel(), login:String, password:String){
    // val myPreferences = PreferenceManager.getDefaultSharedPreferences(context)
   //  val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
     loginViewModel.authIntraru(login, password)
 }
@Composable
fun onCLickButtonSignin(login:String, password:String,  loginViewModel: LoginViewModel = LoginViewModel(), activity: Activity, navController: NavController){
    Log.e("onClick SignIn", "Login screen")
    val authData:List<IntraruAuthUserList> by loginViewModel.authData.observeAsState(emptyList())

    loginViewModel.authData.observe(viewLifecycleOwner, { userData ->
    })


    if(login.isNotEmpty() && login.length ==8){
        if(password.isNotEmpty()){
            loginViewModel.authIntraru(login, password)
            hideKeyboard(activity)
        }else{
            Toast.makeText( activity, "Неправильный логин или пароль", Toast.LENGTH_SHORT).show()
        }
    }else{
        Toast.makeText( activity, "Что-то не то с LDAP", Toast.LENGTH_SHORT).show()
    }
/*
    if (authData.isNotEmpty()) {
        Log.e("Authdtata", authData.toString())
        AuthData(login = login, authData = authData[0], navController=navController)


    }*/

}

@Composable
fun AuthData(login:String,
             authData:IntraruAuthUserList,
             loginViewModel: LoginViewModel = LoginViewModel(),
             navController: NavController
){
    val userData:List<IntraruUserDataList> by loginViewModel.userData.observeAsState(emptyList())
    val t = authData
    val activity = LocalContext.current as Activity
    val sharedPref = activity.getPreferences(Context.MODE_PRIVATE)
    with (sharedPref.edit()) {
        putString("token", t.token)
        putString("refreshToken", t.refreshToken)
        putInt("expiresIn", t.expiresIn)
        putInt("expiresOn", t.expiresOn)
        putString("authHeader", "Bearer " + t.token)
        apply()
    }

    val authHeader = "Bearer " + t.token
    loginViewModel.getInfoUser(login, authHeader)

    if (userData.isNotEmpty()) {
        val uData = userData[0] as IntraruUserDataList
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
        //  userData.clear()
        //  val  userData: List<IntraruUserDataList> = userData



        navController.navigate("search") {
            launchSingleTop = true //переходим только 1 раз
        }

/*
        navController.navigate("search"){
            launchSingleTop = true //переходим только 1 раз
            restoreState = false
        }*/
    }
}





