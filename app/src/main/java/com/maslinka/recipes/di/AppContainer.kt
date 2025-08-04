package com.maslinka.recipes.di

import android.content.Context
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.data.db.RecipesDatabase
import com.maslinka.recipes.data.network.RecipeApiService
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class Module {

    @Provides
    fun provideRetrofit(): Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    @Provides
    fun provideService(retrofit: Retrofit): RecipeApiService = retrofit.create(RecipeApiService::class.java)

    @Provides
    fun provideDb(@ApplicationContext context: Context):RecipesDatabase = RecipesDatabase.getDatabase(context)

    @Provides
    fun provideRepository(service: RecipeApiService, db: RecipesDatabase) = RecipesRepository(service, db)
}