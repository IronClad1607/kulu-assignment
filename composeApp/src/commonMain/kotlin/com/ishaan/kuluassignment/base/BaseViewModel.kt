package com.ishaan.kuluassignment.base

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Base view model class that provides common functionality for view models.
 *
 * @param initialState The initial state of the view model.
 */
abstract class BaseViewModel<S, E>(initialState: S) : ViewModel() {

    // State flow for the current UI state
    protected val _uiState = MutableStateFlow(initialState)
    val uiState = _uiState.asStateFlow()

    /**
     * Safely updates the current UI state using the provided update lambda.
     * Catches and logs any exceptions that occur during the update to prevent crashes.
     */
    protected fun safeUpdateState(update: (S) -> S) {
        try {
            // Perform the state update using the provided lambda
            _uiState.update(update)
        } catch (e: Exception) {
            // Handle and record any exception that occurs during the update using the centralized error utility
            e.printStackTrace()
        }
    }
}