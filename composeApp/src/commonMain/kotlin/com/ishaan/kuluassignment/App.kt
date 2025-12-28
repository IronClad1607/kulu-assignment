package com.ishaan.kuluassignment

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.safeContentPadding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ishaan.kuluassignment.features.movie_list.view.MovieListScreen
import com.ishaan.kuluassignment.navigation.MovieDetailScreen
import com.ishaan.kuluassignment.navigation.MovieListScreen
import com.ishaan.kuluassignment.navigation.MyAppNavHost
import com.ishaan.kuluassignment.theme.MyAppTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview

import kuluassignment.composeapp.generated.resources.Res
import kuluassignment.composeapp.generated.resources.compose_multiplatform

@Composable
fun App() {
    MyAppTheme {
        val navHostController = rememberNavController()
        MyApp(navHostController)
    }
}


@Composable
fun MyApp(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    MyAppNavHost(
        navHostController = navController,
        modifier = modifier.safeDrawingPadding()
    ) {
        composable(
            route = MovieListScreen.route
        ) {
            MovieListScreen(
                navigateToMovieDetail = {

                }
            )
        }

        composable(
            route = MovieDetailScreen.route
        ) {

        }
    }
}