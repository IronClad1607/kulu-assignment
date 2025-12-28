package com.ishaan.kuluassignment.features.movie_detail.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import com.ishaan.kuluassignment.base.ErrorComposable
import com.ishaan.kuluassignment.base.LoadingComposable
import com.ishaan.kuluassignment.features.movie_detail.viewmodel.MovieDetailViewModel
import com.ishaan.kuluassignment.networking.BaseURL
import com.ishaan.kuluassignment.theme.MyAppTheme
import kuluassignment.composeapp.generated.resources.Res
import kuluassignment.composeapp.generated.resources.ic_error_image
import kuluassignment.composeapp.generated.resources.ic_placeholder_image
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.ui.tooling.preview.Preview
import org.koin.compose.viewmodel.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDetailScreen(
    movieId: Long,
    navigateBack: () -> Unit,
    modifier: Modifier = Modifier,
    viewModel: MovieDetailViewModel = koinViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    LaunchedEffect(Unit) {
        viewModel.getMovieDetails(movieId)
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navigateBack()
                        }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        modifier = modifier
    ) { innerPadding ->
        if (uiState.isLoading) {
            LoadingComposable(
                modifier = Modifier.padding(innerPadding)
            )
        } else if (!uiState.error.isNullOrEmpty()) {
            ErrorComposable(
                message = uiState.error ?: "Something went wrong",
                onRetryClicked = {
                    viewModel.getMovieDetails(movieId)
                }
            )
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .verticalScroll(rememberScrollState())
                    .padding(horizontal = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surface
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                ) {
                    AsyncImage(
                        model = "${BaseURL.IMAGE_BASE_URL}${uiState.movie?.backdropPath}",
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize(),
                        placeholder = painterResource(Res.drawable.ic_placeholder_image),
                        error = painterResource(Res.drawable.ic_error_image)
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = uiState.movie?.title ?: "",
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontSize = 24.sp
                    ),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))


                Text(
                    text = uiState.movie?.overview ?: "",
                    style = MaterialTheme.typography.bodyMedium.copy(
                        fontSize = 16.sp,
                        lineHeight = 24.sp
                    ),
                    textAlign = TextAlign.Start,
                    color = MaterialTheme.colorScheme.onBackground,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }
    }
}

@Preview
@Composable
fun MovieDetailScreenPreview() {
    MyAppTheme {
        MovieDetailScreen(
            movieId = 2,
            navigateBack = {}
        )
    }
}