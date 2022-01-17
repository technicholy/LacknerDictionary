package com.technicholy.lacknerdictionary.feature_dictionary.di

import android.app.Application
import androidx.room.Room
import com.google.gson.Gson
import com.technicholy.lacknerdictionary.feature_dictionary.data.local.db.WordInfoDatabase
import com.technicholy.lacknerdictionary.feature_dictionary.data.local.type_converters.Converters
import com.technicholy.lacknerdictionary.feature_dictionary.data.remote.api.DictionaryApi
import com.technicholy.lacknerdictionary.feature_dictionary.data.repository.WordInfoRepositoryImpl
import com.technicholy.lacknerdictionary.feature_dictionary.data.util.GsonParser
import com.technicholy.lacknerdictionary.feature_dictionary.domain.repository.WordInfoRepository
import com.technicholy.lacknerdictionary.feature_dictionary.domain.use_case.GetWordInfo
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WordInfoModule {

    @Provides
    @Singleton
    fun providesGetWordInfoUseCase(
        repository: WordInfoRepository
    ): GetWordInfo {
        return GetWordInfo(repository)
    }

    @Provides
    @Singleton
    fun provideWordInfoRepository(
        db: WordInfoDatabase,
        api: DictionaryApi,
    ): WordInfoRepository {
        return WordInfoRepositoryImpl(api, db.dao)
    }

    @Provides
    @Singleton
    fun provideWordInfoDatabaase(
        app: Application
    ): WordInfoDatabase {
        return Room.databaseBuilder(
            app, WordInfoDatabase::class.java, "word_db"
        )
            .addTypeConverter(Converters(GsonParser(Gson())))
            .build()
    }

    @Provides
    @Singleton
    fun providesDictionaryApi(): DictionaryApi {
        return Retrofit.Builder()
            .baseUrl(DictionaryApi.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(DictionaryApi::class.java)
    }

}