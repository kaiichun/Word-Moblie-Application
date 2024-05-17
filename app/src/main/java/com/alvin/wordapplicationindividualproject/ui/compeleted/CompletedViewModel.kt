package com.alvin.wordapplicationindividualproject.ui.compeleted


import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.alvin.wordapplicationindividualproject.WordApp
import com.alvin.wordapplicationindividualproject.data.model.Word
import com.alvin.wordapplicationindividualproject.data.repository.WordRepo
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch

class CompletedViewModel(private val repo: WordRepo) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _words = MutableLiveData<List<Word>>()
    val words: LiveData<List<Word>> get() = _words

    fun getAllCompletedWords() {
        viewModelScope.launch {
            // Set the loading state to true
            _isLoading.value = true
            // Fetch all words from the repo and filter status
            val filteredWords = repo.getCompletedAll().map { words ->
                words.filter { it.status == true }
            }.first() // Collect the first (and only) emission from the Flow
            // Update the _words LiveData with the filtered list of words
            _words.value = filteredWords
            // Set the loading state to false
            _isLoading.value = false
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApp).repo
                CompletedViewModel(repo)
            }
        }
    }
}
