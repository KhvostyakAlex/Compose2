package ru.leroymerlin.internal.compose2

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.google.accompanist.pager.*
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import ru.leroymerlin.internal.compose2.dataclass.BottomNavItem
import ru.leroymerlin.internal.compose2.ui.screens.*
import ru.leroymerlin.internal.compose2.ui.screens.ListScreen
import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsScreen
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginScreen
import ru.leroymerlin.internal.compose2.ui.screens.login.LoginViewModel
import ru.leroymerlin.internal.compose2.ui.screens.settings.SettingsScreen


import ru.leroymerlin.internal.compose2.ui.theme.Compose2Theme


class MainActivity : ComponentActivity() {
    val loginViewModel by viewModels<LoginViewModel>()
    val cardsViewModel by viewModels<CardsViewModel>()
    @ExperimentalPagerApi
    @ExperimentalMaterialApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Toast.makeText( baseContext, "onCreate", Toast.LENGTH_SHORT).show()


        setContent {
            Compose2Theme (darkTheme = false){
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                   // Greeting("World!!!", "Android")

                    val navController = rememberNavController()
                    //val bottomItems = listOf("list", "search", "push", "cards")
                    val bottomItems = listOf(
                        BottomNavItem("Login", "login", Icons.Default.Home),
                        BottomNavItem("List", "list", Icons.Default.Home),
                        BottomNavItem("Поиск", "search", Icons.Default.Search),
                        BottomNavItem("Настройки", "settings", Icons.Default.Settings))
                    Scaffold(
                        topBar={ TopAppBar(
                            title = { Text(text = stringResource(R.string.app_name), fontSize = 18.sp) },
                            backgroundColor = colorResource(id = R.color.lmNCKD),
                            contentColor = Color.White
                        )},

                        bottomBar = {
                            val backStackEntry = navController.currentBackStackEntryAsState()
                            if(backStackEntry.value?.destination?.route!= "login"){
                                //Log.e("route - ", navController.currentDestination?.route.toString())
                                BottomNavigationBar(items = bottomItems,
                                    navController = navController ,
                                    onItemClick ={
                                        navController.navigate(it.route)
                                    } )
                            }

                        }

                    ) {

                        Navigation(navController = navController, loginViewModel = loginViewModel, cardsViewModel = cardsViewModel )


                        //Greeting("World!!!", "Android")
                        //NavHost( navController = navController, startDestination = "list",)
                    }
                }
            }
        }
    }


}

@ExperimentalMaterialApi
@ExperimentalPagerApi
@Composable
fun Navigation(navController: NavHostController,
               loginViewModel: LoginViewModel,
               cardsViewModel: CardsViewModel){
    NavHost(navController = navController, startDestination = "search"){
        composable("login"){ LoginScreen(loginViewModel, navController)}
        composable("list"){ ListScreen(navController)}
        composable("search"){ SearchScreen()}
        composable("cards"){ CardsScreen(cardsViewModel) }
        composable("details"){ DetailsScreen()}
        composable("settings"){ SettingsScreen(navController) }
    }

}

@ExperimentalMaterialApi
@Composable
fun BottomNavigationBar(
    items: List<BottomNavItem>,
    navController: NavController,
    modifier: Modifier = Modifier,
    onItemClick:(BottomNavItem) -> Unit

){
    val backStackEntry = navController.currentBackStackEntryAsState()
    BottomNavigation(
        modifier = modifier,
        backgroundColor = Color.White,
        elevation = 5.dp
    ){
      items.forEach{ item ->
          val selected = item.route == backStackEntry.value?.destination?.route
          BottomNavigationItem(selected = item.route == navController.currentDestination?.route,
              onClick = { onItemClick(item) },
              selectedContentColor = colorResource(id = R.color.lmNCKD),
              unselectedContentColor = Color.Gray,
              icon = {
                    Column(horizontalAlignment = CenterHorizontally){
                        if(item.badgeCount >0){
                            BadgeBox(
                               badgeContent = {
                                   Text(text = item.badgeCount.toString())
                               }
                            ) {
                                Icon(imageVector = item.icon, contentDescription = item.name)
                            }
                        }else{
                            Icon(imageVector = item.icon, contentDescription = item.name)
                        }
                        if(selected){
                            Text(text = item.name,
                            textAlign = TextAlign.Center,
                            fontSize = 10.sp)
                        }
                    }
              }
          )
      }
    }
}




@Composable
fun Greeting(name: String, lastName: String) {
    Column{
        Text(text = "Hello $name!",
        modifier = Modifier.padding(bottom = 8.dp),
        style = MaterialTheme.typography.h5)
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = "Hello $lastName!")
        OutlinedTextField(value = name,
            onValueChange = {  it},
        label = {Text("Name")})
    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    Compose2Theme {
        Greeting("Android", "world")

    }
}





