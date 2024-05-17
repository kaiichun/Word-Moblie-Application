package com.alvin.wordapplicationindividualproject.ui.container

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch

class ContainerViewModel: ViewModel() {
    val refreshHome: MutableSharedFlow<Unit> = MutableSharedFlow()

    fun refreshHome() {
        viewModelScope.launch {
            refreshHome.emit(Unit)
        }
    }
}