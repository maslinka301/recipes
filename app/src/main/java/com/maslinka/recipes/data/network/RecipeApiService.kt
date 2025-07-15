package com.maslinka.recipes.data.network

import com.maslinka.recipes.model.Category
import com.maslinka.recipes.model.Recipe
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RecipeApiService {
    @GET("category")
    fun getCategories(): Call<List<Category>>

    @GET("category/{id}/recipes")
    fun getRecipesByCategoryId(@Path("id") id: String?): Call<List<Recipe>>

    @GET("category/{id}")
    fun getCategory(@Path("id") id: String?): Call<Category>

    @GET("recipe/{id}")
    fun getRecipeById(@Path("id") id: String): Call<Recipe>

    @GET("recipes")
    fun getRecipesByIds(@Query("ids") ids: String): Call<List<Recipe>>
}

