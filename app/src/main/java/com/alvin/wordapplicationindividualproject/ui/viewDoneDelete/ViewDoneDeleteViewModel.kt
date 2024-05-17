package com.alvin.wordapplicationindividualproject.ui.viewDoneDelete

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
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ViewDoneDeleteViewModel (
    private val repo: WordRepo
) : ViewModel() {

    private val _word: MutableLiveData<Word?> = MutableLiveData()
    val selectedWord: LiveData<Word?> = _word
    private val _isLoading: MutableLiveData<Boolean> = MutableLiveData()
    val isLoading: LiveData<Boolean> get() = _isLoading
    val finish :MutableSharedFlow<Unit> = MutableSharedFlow()
    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonym: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Set loading state to true
                _isLoading.postValue(true)
                // Retrieve the word by id from the repository
                val word = repo.getWordById(id)
                delay(100)
                // Post the retrieved word to LiveData
                _word.postValue(word)
            } catch (e: Exception) {
                // Post an error message to the snackbar LiveData
                withContext(Dispatchers.Main) {
                    snackbar.value = "Error retrieving word: ${e.message}"
                }
            } finally {
                // Set loading state to false
                _isLoading.postValue(false)
            }
        }
    }

    fun moveWordToAnywhere() {
        viewModelScope.launch(Dispatchers.IO) {
            val currentWord = selectedWord.value
            if (currentWord != null) {
                val updatedStatus = !currentWord.status!!
                val updatedWord = currentWord.copy(status = updatedStatus)
                repo.updateWord(updatedWord)
                finish.emit(Unit)
            }
        }
    }

    fun delete() {
        viewModelScope.launch (Dispatchers.IO){
            repo.deleteWord(selectedWord.value!!)
            finish.emit(Unit)
        }
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApp).repo
                ViewDoneDeleteViewModel(repo)
            }
        }
    }
}