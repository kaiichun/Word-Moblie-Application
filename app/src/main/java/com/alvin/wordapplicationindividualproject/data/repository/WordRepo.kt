package com.alvin.wordapplicationindividualproject.data.repository

import com.alvin.wordapplicationindividualproject.data.database.WordDao
import com.alvin.wordapplicationindividualproject.data.model.Word
import kotlinx.coroutines.flow.Flow

class WordRepo(private val dao: WordDao) {

    fun getAll(): Flow<List<Word>> {
        return dao.getAll()
    }

    fun getCompletedAll(): Flow<List<Word>> {
        return dao.getCompletedAll()
    }

    fun getWordById(id: Int): Word? {
        return dao.getWordById(id)
    }

    fun addWord(word: Word) {
        dao.addWord(word)
    }

    fun updateWord(word: Word) {
        dao.updateWord(word)
    }

    fun deleteWord(word: Word) {
        dao.deleteWord(word)
    }

}