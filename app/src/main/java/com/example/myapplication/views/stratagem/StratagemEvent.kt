package com.example.myapplication.views.stratagem

sealed interface StratagemEvent {
    data class UpdateField(val field: String, val value: String) : StratagemEvent
    object Save : StratagemEvent;
    object NavigateBack : StratagemEvent
    object DeleteStratagem : StratagemEvent
}