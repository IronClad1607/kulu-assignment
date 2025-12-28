package com.ishaan.kuluassignment.features.movie_list.view

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import com.ishaan.kuluassignment.theme.MyAppTheme
import org.jetbrains.compose.ui.tooling.preview.Preview

// Search Bar Composable
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBar(
    searchText: String,
    onSearchTextChange: (String) -> Unit,
    onCloseClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    // FocusRequester ensures the search text field gets focused automatically when opened
    val focusRequester = remember { FocusRequester() }
    // TopAppBar layout hosting the search icon, search text field, and close button
    TopAppBar(
        navigationIcon = {
            // Leading search icon inside the search bar
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                modifier = Modifier.padding(8.dp)
            )
        },
        title = {
            // TextField where the user types the movie name to search
            TextField(
                value = searchText,
                onValueChange = onSearchTextChange,
                placeholder = {
                    // Placeholder shown when no text is entered
                    Text(
                        text = "Search Movie...",
                        style = MaterialTheme.typography.bodyMedium
                    )
                },
                // Makes the TextField visually blend into the TopAppBar by removing background and indicators
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    disabledIndicatorColor = Color.Transparent,
                ),
                textStyle = MaterialTheme.typography.bodyMedium,
                singleLine = true,
                // Configure keyboard behavior: capitalize sentences and trigger search on IME action
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Sentences,
                    imeAction = ImeAction.Search
                ),
                modifier = modifier
                    .fillMaxWidth()
                    .focusRequester(focusRequester)
            )
        },
        // Close button to clear the search text and collapse the search bar
        actions = {
            IconButton(
                // When clicked, clears search text and triggers close action
                onClick = {
                    onCloseClick()
                    onSearchTextChange("")
                }
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Search"
                )
            }
        }
    )

    // Automatically request focus when SearchBar appears
    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}

@Preview
@Composable
fun SearchBarPreview(
    text: String = "",
    onTextChange: (String) -> Unit = {}
) {
    MyAppTheme {
        SearchBar(
            searchText = text,
            onSearchTextChange = onTextChange,
            onCloseClick = {}
        )
    }
}