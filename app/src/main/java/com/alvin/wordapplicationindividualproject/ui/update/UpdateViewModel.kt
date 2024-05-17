package com.alvin.wordapplicationindividualproject.ui.update

import android.util.Log
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
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.util.Date

class UpdateViewModel(
    private val repo: WordRepo
) : ViewModel() {
    private val _word : MutableLiveData<Word> = MutableLiveData()
    val selectedWord: LiveData<Word> = _word
    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonym: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun getWordById(id: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _word.postValue(repo.getWordById(id))
        }
    }

    fun setWord(word: Word) {
        word?. let {
            title.value =it.title
            meaning.value= it.meaning
            details.value = it.details
            synonym.value= it.synonym
        }

    }

    fun update() {
        // Check if any of the required fields are empty
        if (title.value.isNullOrEmpty() || meaning.value.isNullOrEmpty() || synonym.value.isNullOrEmpty() || details.value.isNullOrEmpty()) {
            snackbar.value = "All fields cannot be empty"
            return
        }

        // Launch a coroutine in the ViewModel scope with IO dispatcher
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val word = selectedWord.value
                if (word != null) {
                    // Check if any field has been changed
                    val hasChanges = word.title != title.value ||
                            word.meaning != meaning.value ||
                            word.synonym != synonym.value ||
                            word.details != details.value

                    if (!hasChanges) {
                        withContext(Dispatchers.Main) {
                            snackbar.value = "No changes detected"
                            finish.emit(Unit)
                        }
                        // Return to avoid updating the word if no changes
                        return@launch
                    }

                    // Perform the update if there are changes
                    repo.updateWord(
                        word.copy(
                            title = title.value!!,
                            meaning = meaning.value!!,
                            synonym = synonym.value!!,
                            details = details.value!!,
                            date = LocalDateTime.now()
                        )
                    )
                    // Switch to the Main dispatcher to update UI-related LiveData
                    withContext(Dispatchers.Main) {
                        snackbar.value = "Update Successfully"
                        finish.emit(Unit)
                    }
                }
            } catch (e: Exception) {
                // Switch to the Main dispatcher to update LiveData with error message
                withContext(Dispatchers.Main) {
                    snackbar.value = "Error updating word: ${e.message}"
                    finish.emit(Unit)
                }
            }
        }
        Log.d("update", "${title.value}, ${meaning.value}, ${synonym.value}, ${details.value}")
    }

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val repo =
                    (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as WordApp).repo
                UpdateViewModel(repo)
            }
        }
    }
}