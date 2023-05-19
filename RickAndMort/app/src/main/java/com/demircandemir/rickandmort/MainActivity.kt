package com.demircandemir.rickandmort

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.demircandemir.rickandmort.ui.theme.RickAndMortTheme
import com.demircandemir.rickandmort.view.DetailScreen
import com.demircandemir.rickandmort.view.ListScreen
import com.demircandemir.rickandmort.view.SplashScreen
import com.demircandemir.rickandmort.view.SplashScreen2
import com.demircandemir.rickandmort.viewModel.ListViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RickAndMortTheme {

                val shared : SharedPreferences = getSharedPreferences("Info", Context.MODE_PRIVATE)



                if(shared.getInt("flag", 0) == 0) {
                    val editor = shared.edit()
                    editor.putInt("flag", 1).apply()
                    //SPLASH1

                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash_screen"
                    ) {



                        composable("splash_screen") {
                            SplashScreen(navController = navController)
                        }


                        composable("list_screen") {
                            ListScreen( navController,
                                onCharacterItemClick = { characterId, characterName ->
                                    navController.navigate("character_detail_screen/$characterId/$characterName")
                                }
                            )
                        }
                        composable("character_detail_screen/{characterId}/{characterName}",
                            arguments = listOf(
                                navArgument("characterId") {
                                    type = NavType.StringType
                                },
                                navArgument("characterName") {
                                    type = NavType.StringType
                                }
                            )
                        ){
                            val characterId = remember {
                                it.arguments?.getString("characterId")
                            }
                            val characterName = remember {
                                it.arguments?.getString("characterName")
                            }
                            DetailScreen(
                                id = characterId ?: "",
                                characterName =  characterName ?: "",
                                navController = navController,
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }


                } else {
                    val navController = rememberNavController()
                    NavHost(
                        navController = navController,
                        startDestination = "splash_screen2"
                    ) {



                        composable("splash_screen2") {
                            SplashScreen2(navController = navController)
                        }


                        composable("list_screen") {
                            ListScreen( navController,
                                onCharacterItemClick = { characterId, characterName ->
                                    navController.navigate("character_detail_screen/$characterId/$characterName")
                                }
                            )
                        }
                        composable("character_detail_screen/{characterId}/{characterName}",
                            arguments = listOf(
                                navArgument("characterId") {
                                    type = NavType.StringType
                                },
                                navArgument("characterName") {
                                    type = NavType.StringType
                                }
                            )
                        ){
                            val characterId = remember {
                                it.arguments?.getString("characterId")
                            }
                            val characterName = remember {
                                it.arguments?.getString("characterName")
                            }
                            DetailScreen(
                                id = characterId ?: "",
                                characterName =  characterName ?: "",
                                navController = navController,
                                onBackClick = { navController.navigateUp() }
                            )
                        }
                    }
                }




            }
        }
    }
}

