package com.ishaan.kuluassignment

import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ishaan.kuluassignment.features.movie_detail.view.MovieDetailScreen
import com.ishaan.kuluassignment.features.movie_list.view.MovieListScreen
import com.ishaan.kuluassignment.navigation.MovieDetailScreen
import com.ishaan.kuluassignment.navigation.MovieListScreen
import com.ishaan.kuluassignment.navigation.MyAppNavHost
import com.ishaan.kuluassignment.theme.MyAppTheme

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
                navigateToMovieDetail = { movieId ->
                    val route = MovieDetailScreen.createRoute(movieId)
                    navController.navigate(route)
                }
            )
        }

        composable(
            route = MovieDetailScreen.routeWithArgs,
            arguments = MovieDetailScreen.arguments
        ) { backStackEntry ->
            val movieId = backStackEntry.savedStateHandle.get<Long>(MovieDetailScreen.MOVIE_ID_ARGS)
                ?: throw IllegalArgumentException("Movie ID is required")
            MovieDetailScreen(
                movieId = movieId,
                navigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}