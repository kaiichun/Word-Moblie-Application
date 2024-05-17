package com.alvin.wordapplicationindividualproject.ui.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
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
import java.util.Date

class AddViewModel(private val repo: WordRepo):ViewModel() {

    val title: MutableLiveData<String> = MutableLiveData()
    val meaning: MutableLiveData<String> = MutableLiveData()
    val synonym: MutableLiveData<String> = MutableLiveData()
    val details: MutableLiveData<String> = MutableLiveData()
    val finish: MutableSharedFlow<Unit> = MutableSharedFlow()
    val snackbar: MutableLiveData<String> = MutableLiveData()

    fun submit() {
        // Check if any of the required fields are empty
        if (title.value.isNullOrEmpty() || meaning.value.isNullOrEmpty() || synonym.value.isNullOrEmpty() || details.value.isNullOrEmpty()) {
            snackbar.value = "All fields cannot be empty"
            return
        }

        // Launch a coroutine in the ViewModel scope with IO dispatcher
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Ensure none of the fields are null
                if (title.value != null && meaning.value != null && synonym.value != null && details.value != null) {
                    // Add the new word to the repository
                    repo.addWord(
                        Word(
                            title = title.value!!,
                            meaning = meaning.value!!,
                            synonym = synonym.value!!,
                            details = details.value!!,
                            status = false
                        )
                    )
                    // Switch to the Main dispatcher to update UI-related LiveData
                    withContext(Dispatchers.Main) {
                        snackbar.value = "Add New Word Successfully"
                    }
                    finish.emit(Unit)
                }
            } catch (e: Exception) {
                // Switch to the Main dispatcher to update UI-related LiveData with error message
                withContext(Dispatchers.Main) {
                    snackbar.value = "Error adding word: ${e.message}"
                    finish.emit(Unit)
                }
            }
        }
        Log.d("submit", "${title.value}, ${meaning.value}, ${synonym.value}, ${details.value}")
    }


    companion object {
        val Factory = viewModelFactory {
            initializer {
                AddViewModel(
                    (this[APPLICATION_KEY] as WordApp).repo
                )
            }
        }
    }
}