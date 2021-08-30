package ru.leroymerlin.internal.compose2

import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHost
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import cru.leroymerlin.internal.compose2.ui.screens.cards.CardsViewModel
import dagger.hilt.android.AndroidEntryPoint
import ru.leroymerlin.internal.compose2.repository.PhoneBookRepository
import ru.leroymerlin.internal.compose2.ui.screens.*
import ru.leroymerlin.internal.compose2.ui.screens.ListScreen
import ru.leroymerlin.internal.compose2.ui.screens.cards.CardsScreen


import ru.leroymerlin.internal.compose2.ui.theme.Compose2Theme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val cardsViewModel by viewModels<CardsViewModel>()

        setContent {
            Compose2Theme (darkTheme = false){
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                   // Greeting("World!!!", "Android")

                    val navController = rememberNavController()
                    val bottomItems = listOf("list", "search", "push", "cards")
                    Scaffold(
                        bottomBar = {
                            BottomNavigation {
                                bottomItems.forEach{screen ->
                                    BottomNavigationItem(selected = false,
                                        onClick = { navController.navigate(screen) },
                                    label = {Text(screen)},
                                    icon={

                                    })


                                }
                            }
                        }
                    ) {
                        NavHost(navController = navController,
                            startDestination = "login"){
                            composable("login"){ LoginScreen(navController)}
                            composable("list"){ ListScreen(navController)}
                            composable("search"){ SearchScreen(navController, cardsViewModel)}
                            composable("push"){ PushScreen()}
                            composable("cards"){ CardsScreen(cardsViewModel) }
                            composable("details"){ DetailsScreen()}
                        }
                        //Greeting("World!!!", "Android")
                        //NavHost( navController = navController, startDestination = "list",)
                    }
                }
            }
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