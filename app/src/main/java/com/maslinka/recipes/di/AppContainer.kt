package com.maslinka.recipes.di

import android.content.Context
import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.data.db.RecipesDatabase
import com.maslinka.recipes.data.network.RecipeApiService
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory

class AppContainer(context: Context) {
    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db = RecipesDatabase.getDatabase(context)

    val repository = RecipesRepository(service, db)
}