package ru.leroymerlin.internal.compose2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
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
import ru.leroymerlin.internal.compose2.ui.screens.DetailsScreen
import ru.leroymerlin.internal.compose2.ui.screens.ListScreen
import ru.leroymerlin.internal.compose2.ui.screens.PushScreen
import ru.leroymerlin.internal.compose2.ui.screens.SearchScreen


import ru.leroymerlin.internal.compose2.ui.theme.Compose2Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Compose2Theme (darkTheme = false){
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                   // Greeting("World!!!", "Android")

                    val navController = rememberNavController()
                    val bottomItems = listOf("list", "search", "push")
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
                            startDestination = "list"){
                            composable("list"){ ListScreen(navController)}
                            composable("search"){ SearchScreen(navController)}
                            composable("push"){ PushScreen()}
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