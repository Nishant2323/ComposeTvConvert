package com.example.composeconvert

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.composeconvert.DashBoard.DashboardScreen

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun App (modifier: Modifier = Modifier, onBackPressed: () -> Unit) {
    val navController = rememberNavController()
    var isComingBackFromDifferentScreen by remember { mutableStateOf(false) }
  NavHost(navController = navController, startDestination = Screens.Dashboard()){
        composable(route = Screens.SpashScreen()){

        }
        composable(route = Screens.Dashboard()){
            DashboardScreen(
                openCategoryMovieList = { categoryId ->
                    navController.navigate(
                        Screens.CategoryMovieList.withArgs(categoryId)
                    )
                },
                openMovieDetailsScreen = { movieId ->
                    navController.navigate(
                        Screens.MovieDetails()

                    )
                },
                openVideoPlayer = {
                    navController.navigate(Screens.VideoPlayer())
                },
                onBackPressed = onBackPressed,
                isComingBackFromDifferentScreen = isComingBackFromDifferentScreen,
                resetIsComingBackFromDifferentScreen = {
                    isComingBackFromDifferentScreen = false
                }
            )
        }


        composable(
            route = Screens.CategoryMovieList(),
            arguments = listOf(
                navArgument("categoryId") {
                    type = NavType.StringType
                }
            )
        ){

        }


        composable(route = Screens.VideoPlayer()){}


        composable(
            route = Screens.MovieDetails(),
        ){
            MovieDetailsScreen(
                goToMoviePlayer = {
                    navController.navigate(Screens.VideoPlayer())
                },

                onBackPressed = {
                    if (navController.navigateUp()) {
                        isComingBackFromDifferentScreen = true
                    }
                }
            )

        }
    }
}