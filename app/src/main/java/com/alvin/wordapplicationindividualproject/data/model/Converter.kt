package com.alvin.wordapplicationindividualproject.data.model

import androidx.room.TypeConverter
import java.time.LocalDateTime

class Converter {
    // This function is to convert a database type to a Kotlin type
    @TypeConverter
    fun fromTimestamp(value: String?): LocalDateTime? = value?.let {
        // If 'value' is not null, parse it to LocalDateTime
        LocalDateTime.parse(it)
    }
    // This function is to convert a Kotlin type to a database type
    @TypeConverter
    // If 'date' is not null, convert it to its String representation
    fun dateToTimestamp(date: LocalDateTime?): String? = date?.toString()

}