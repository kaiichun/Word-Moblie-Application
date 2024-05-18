package com.alvin.wordapplicationindividualproject.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.alvin.wordapplicationindividualproject.data.model.Word
import kotlinx.coroutines.flow.Flow
import java.util.Date


@Dao
interface WordDao {

    @Query("SELECT * FROM Word WHERE status = 0 ORDER BY date DESC")
    fun getAll(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE status = 1 ORDER BY date DESC")
    fun getCompletedAll(): Flow<List<Word>>

    @Query("SELECT * FROM Word WHERE id = :id")
    fun getWordById(id: Int): Word?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addWord(word: Word)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateWord(word: Word)

    @Delete
    fun deleteWord(word: Word)

}