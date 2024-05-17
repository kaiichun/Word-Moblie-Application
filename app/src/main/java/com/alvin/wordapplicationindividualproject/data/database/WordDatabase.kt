package com.alvin.wordapplicationindividualproject.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.alvin.wordapplicationindividualproject.data.model.Converter
import com.alvin.wordapplicationindividualproject.data.model.Word

@Database(entities = [Word::class], version = 2)
// Import converter
@TypeConverters(Converter::class)
abstract class WordDatabase: RoomDatabase() {
    abstract fun workDao(): WordDao

    companion object {
        const val NAME = "my_word_database"
    }
}