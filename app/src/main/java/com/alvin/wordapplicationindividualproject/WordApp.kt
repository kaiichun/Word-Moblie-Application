package com.alvin.wordapplicationindividualproject

import android.app.Application
import androidx.room.Room
import com.alvin.wordapplicationindividualproject.data.database.WordDatabase
import com.alvin.wordapplicationindividualproject.data.repository.WordRepo

class WordApp : Application() {
    lateinit var repo: WordRepo

    override fun onCreate() {
        super.onCreate()
        val database = Room.databaseBuilder(
            this,
            WordDatabase::class.java,
            WordDatabase.NAME
        ).fallbackToDestructiveMigration().build()

        repo = WordRepo(database.workDao())
    }
}