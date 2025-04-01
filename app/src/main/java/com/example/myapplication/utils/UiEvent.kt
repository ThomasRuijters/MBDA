package com.example.myapplication.utils

sealed interface UiEvent {
    data class Navigate(val route: String) : UiEvent
    object NavigateBack : UiEvent
}