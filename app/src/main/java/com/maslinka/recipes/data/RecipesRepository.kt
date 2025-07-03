package com.maslinka.recipes.data

import android.content.Context
import com.maslinka.recipes.model.Category
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import android.util.Log
import com.maslinka.recipes.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository(context: Context) {

    private var retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://recipes.androidsprint.ru/api/")
        //.addConverterFactory(GsonConverterFactory.create())
        .addConverterFactory(Json.asConverterFactory("application/json; charset=UTF8".toMediaType()))
        .build()

    private var service: RecipeApiService = retrofit.create(RecipeApiService::class.java)

    private val db = RecipesDatabase.getDatabase(context)

    suspend fun getCategories(): List<Category>? {
        return withContext(Dispatchers.IO) {
            try {
                val categories: Response<List<Category>> = service.getCategories().execute()
                Log.d("!!!", categories.code().toString())
                val result = if (categories.isSuccessful) categories.body() else null
                if (result != null) {
                    db.categoriesDao().addCategories(result)
                }
                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRecipesByCategoryId(id: Int): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipesByCategoryId(id.toString()).execute()
                Log.d("!!!", response.code().toString())
                val result = if (response.isSuccessful) response.body() else null
                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getCategory(id: Int): Category? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getCategory(id.toString()).execute()
                val result = if (response.isSuccessful) response.body() else null
                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRecipeById(id: Int): Recipe? {
        return withContext(Dispatchers.IO) {
            try {
                val response = service.getRecipeById(id.toString()).execute()
                val result = if (response.isSuccessful) response.body() else null
                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getRecipesByIds(ids: MutableSet<Int>): List<Recipe>? {
        return withContext(Dispatchers.IO) {
            try {
                val idsStr = ids.joinToString(",")
                Log.d("!!!", "Запрос рецептов $idsStr")
                val response = service.getRecipesByIds(idsStr).execute()
                val call = service.getRecipesByIds(idsStr)
                Log.d(
                    "!!!",
                    "код: ${response.code()}, тело: ${response.body()}, url ${call.request()}"
                )
                val result = if (response.isSuccessful) response.body() else null
                result
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }

    suspend fun getCategoriesFromCache(): List<Category>{
        return withContext(Dispatchers.IO){
            db.categoriesDao().getAllCategories()
        }
    }
}