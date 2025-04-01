package com.example.myapplication.views.stratagem_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.domain.model.Stratagem
import com.example.myapplication.domain.repository.StratagemRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class StratagemListViewModel(
    private val repository: StratagemRepository,
) : ViewModel() {

    private val _stratagems = MutableStateFlow<List<Stratagem>>(emptyList())
    val stratagems: StateFlow<List<Stratagem>> = _stratagems

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    fun loadStratagems() {
        viewModelScope.launch {
            val savedStratagems = repository.getLocalStratagems()

            if (savedStratagems.isNotEmpty()) {
                _stratagems.value = savedStratagems
                return@launch
            }

            repository.fetch(
                onResponse = { fetchedStratagems ->
                    viewModelScope.launch {
                        _stratagems.value = fetchedStratagems
                    }
                },
                onError = { errorMessage ->
                    viewModelScope.launch {
                        if (_stratagems.value.isEmpty()) {
                            _error.value = errorMessage
                        }
                    }
                }
            )
        }
    }
}
