package com.example.myapplication.views.stratagem

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Stratagem
import com.example.myapplication.domain.repository.StratagemRepository
import com.example.myapplication.utils.UiEvent
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class StratagemViewModel(
    private val repository: StratagemRepository,
    private val stratagemId: Int?
) : ViewModel() {

    private val _state = MutableStateFlow(StratagemState())
    val state = _state.asStateFlow()

    private val _event = Channel<UiEvent>()
    val event = _event.receiveAsFlow()

    private fun sendEvent(event: UiEvent) {
        viewModelScope.launch {
            _event.send(event)
        }
    }

    init {
        viewModelScope.launch {
            repository.getLocalStratagemById(stratagemId)?.let { stratagem ->
                _state.update { screenState ->
                    screenState.copy(
                        id = stratagem.id,
                        codename = stratagem.codename,
                        name = stratagem.name,
                        keys = stratagem.keys,
                        uses = stratagem.uses,
                        cooldown = stratagem.cooldown!!,
                        activation = stratagem.activation,
                        imageUrl = stratagem.imageUrl,
                        groupId = stratagem.groupId!!,
                        createdAt = stratagem.createdAt,
                        updatedAt = stratagem.updatedAt
                    )
                }
            }
        }
    }

    fun onEvent(event: StratagemEvent) {
        when (event) {
            is StratagemEvent.UpdateField -> {
                viewModelScope.launch {
                    _state.value = _state.value.copy(
                        name = when (event.field) {
                            "name" -> event.value
                            else -> _state.value.name
                        },
                        codename = when (event.field) {
                            "codename" -> event.value
                            else -> _state.value.codename
                        },
                        uses = when (event.field) {
                            "uses" -> event.value
                            else -> _state.value.uses
                        },
                        cooldown = when (event.field) {
                            "cooldown" -> event.value.toIntOrNull() ?: 0
                            else -> _state.value.cooldown
                        },
                        activation = when (event.field) {
                            "activation" -> event.value.toIntOrNull() ?: 0
                            else -> _state.value.activation
                        },
                        imageUrl = when (event.field) {
                            "imageUrl" -> event.value
                            else -> _state.value.imageUrl
                        },
                        groupId = when (event.field) {
                            "groupId" -> event.value.toIntOrNull() ?: 0
                            else -> _state.value.groupId
                        },
                        createdAt = when (event.field) {
                            "createdAt" -> event.value
                            else -> _state.value.createdAt
                        },
                        updatedAt = when (event.field) {
                            "updatedAt" -> event.value
                            else -> _state.value.updatedAt
                        },
                        id = when (event.field) {
                            "id" -> event.value.toIntOrNull()
                            else -> _state.value.id
                        }
                    )
                }
            }

            StratagemEvent.NavigateBack -> sendEvent(UiEvent.NavigateBack)

            StratagemEvent.Save -> {
                viewModelScope.launch {
                    val state = state.value

                    val stratagem = Stratagem(
                        id = state.id,
                        codename = state.codename,
                        name = state.name,
                        keys = state.keys,
                        uses = state.uses,
                        cooldown = state.cooldown,
                        activation = state.activation,
                        imageUrl = state.imageUrl,
                        groupId = state.groupId,
                        createdAt = state.createdAt,
                        updatedAt = state.updatedAt
                    )

                    if (state.id == null) {
                        repository.addStratagem(stratagem)
                    } else {
                        repository.updateStratagem(stratagem)
                    }
                    sendEvent(UiEvent.NavigateBack)
                }
            }

            StratagemEvent.DeleteStratagem -> {
                viewModelScope.launch {
                    val state = state.value
                    repository.deleteStratagem(state.id ?: return@launch)
                    sendEvent(UiEvent.NavigateBack)
                }
            }
        }
    }
}