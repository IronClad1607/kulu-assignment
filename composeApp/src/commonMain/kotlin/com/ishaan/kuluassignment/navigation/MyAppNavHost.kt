package com.ishaan.kuluassignment.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost

@Composable
fun MyAppNavHost(
    navHostController: NavHostController,
    modifier: Modifier = Modifier,
    builder: NavGraphBuilder.() -> Unit
) {
    NavHost(
        navController = navHostController,
        startDestination = MovieListScreen.route,
        // Define transition when navigating to a new screen
        enterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(700)
            ) + fadeIn(animationSpec = tween(700))
        },
        // Define transition when navigating away from a screen
        exitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(700)
            ) + fadeOut(animationSpec = tween(700))
        },
        // Define transition when navigating back to a previous screen
        popEnterTransition = {
            slideInHorizontally(
                initialOffsetX = { fullWidth -> -fullWidth },
                animationSpec = tween(700)
            ) + fadeIn(animationSpec = tween(700))
        },
        // Define transition when popping back and leaving a screen
        popExitTransition = {
            slideOutHorizontally(
                targetOffsetX = { fullWidth -> fullWidth },
                animationSpec = tween(700)
            ) + fadeOut(animationSpec = tween(700))
        },
        builder = builder,
        modifier = modifier
    )
}