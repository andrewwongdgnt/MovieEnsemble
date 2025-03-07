package com.dgnt.movienensemble.featureMovie.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.dgnt.movienensemble.featureMovie.data.local.converter.MovieListConverter
import com.dgnt.movienensemble.featureMovie.data.local.dao.SearchResultDao
import com.dgnt.movienensemble.featureMovie.data.local.entity.SearchResultEntity

@Database(
    entities = [SearchResultEntity::class],
    version = 1
)
@TypeConverters(MovieListConverter::class)
abstract class MovieEnsembleDatabase : RoomDatabase() {
    abstract val searchResultDao: SearchResultDao
}