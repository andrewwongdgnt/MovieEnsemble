package com.dgnt.movienensemble.featureMovie.di

import android.app.Application
import androidx.room.Room
import com.dgnt.movienensemble.core.util.serializer.Serializer
import com.dgnt.movienensemble.core.util.serializer.SerializerImpl
import com.dgnt.movienensemble.featureMovie.data.local.MovieEnsembleDatabase
import com.dgnt.movienensemble.featureMovie.data.local.converter.MovieListConverter
import com.dgnt.movienensemble.featureMovie.data.local.dao.SearchResultDao
import com.dgnt.movienensemble.featureMovie.data.remote.OMDBApi
import com.dgnt.movienensemble.featureMovie.data.repository.SearchResultRepositoryImpl
import com.dgnt.movienensemble.featureMovie.domain.repository.SearchResultRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideSearchResultRepository(
        api: OMDBApi,
        dao: SearchResultDao,
    ): SearchResultRepository {
        return SearchResultRepositoryImpl(api, dao)
    }

    @Provides
    @Singleton
    fun provideSearchResultDao(
        db: MovieEnsembleDatabase
    ): SearchResultDao {
        return db.searchResultDao
    }

    @Provides
    @Singleton
    fun provideDatabase(
        app: Application,
        movieListConverter: MovieListConverter
    ): MovieEnsembleDatabase {
        return Room.databaseBuilder(
            app, MovieEnsembleDatabase::class.java, "movie_ensemble_db"
        ).addTypeConverter(movieListConverter)
            .build()
    }

    @Provides
    @Singleton
    fun provideSerializer(): Serializer =
        SerializerImpl()

    @Provides
    @Singleton
    fun provideMovieListConverter(serializer: Serializer) =
        MovieListConverter(serializer)

    @Provides
    @Singleton
    fun provideOMDBApi(): OMDBApi {
        return Retrofit.Builder()
            .baseUrl(OMDBApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OMDBApi::class.java)
    }
}