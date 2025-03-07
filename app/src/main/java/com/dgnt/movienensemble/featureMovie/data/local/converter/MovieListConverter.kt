package com.dgnt.movienensemble.featureMovie.data.local.converter

import androidx.room.ProvidedTypeConverter
import androidx.room.TypeConverter
import com.dgnt.movienensemble.core.util.serializer.Serializer
import com.dgnt.movienensemble.featureMovie.data.local.entity.MovieData
import com.google.gson.reflect.TypeToken

@ProvidedTypeConverter
class MovieListConverter(private val serializer: Serializer) {
    @TypeConverter
    fun fromJson(json: String): List<MovieData> {
        return try {
            serializer.deserialize(json, object : TypeToken<MovieData>() {}.type)
        } catch (e: Exception) {
            emptyList()
        }
    }

    @TypeConverter
    fun toJson(value: List<MovieData>): String {
        return serializer.serialize(value)
    }
}