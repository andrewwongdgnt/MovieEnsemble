package com.dgnt.movienensemble.featureMovie.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.dgnt.movienensemble.featureMovie.data.local.entity.SearchResultEntity

@Dao
interface SearchResultDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: SearchResultEntity)

    @Query("SELECT * FROM searchresultentity WHERE searchQuery = :searchQuery AND currentPage = :currentPage")
    suspend fun get(searchQuery: String, currentPage: Int): SearchResultEntity?
}