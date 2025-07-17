package com.maslinka.recipes.data

import com.maslinka.recipes.model.Category
import retrofit2.Response
import android.util.Log
import com.maslinka.recipes.data.db.RecipesDatabase
import com.maslinka.recipes.data.network.RecipeApiService
import com.maslinka.recipes.model.Recipe
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RecipesRepository(
    private val service: RecipeApiService,
    private val db: RecipesDatabase
) {


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
                if (result != null) {
                    val resultForCache = result.map { it.copy(categoryId = id) }
                    val favouriteList = db.recipesDao().getFavouritesRecipes()
                    val favouriteIds = favouriteList.map { it.id }.toSet()
                    val newResultForCache =
                        resultForCache.map { it.copy(isFavorite = it.id in favouriteIds) }
                    db.recipesDao().addRecipes(newResultForCache)
                }
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

    suspend fun getCategoriesFromCache(): List<Category> {
        return withContext(Dispatchers.IO) {
            db.categoriesDao().getAllCategories()
        }
    }

    suspend fun getRecipesFromCache(categoryId: Int): List<Recipe> {
        return withContext(Dispatchers.IO) {
            db.recipesDao().getRecipesByCategoryId(categoryId)
        }
    }

    suspend fun getFavouritesRecipesFromCache(): List<Recipe> {
        return withContext(Dispatchers.IO) {
            db.recipesDao().getFavouritesRecipes()
        }
    }

    suspend fun updateFavouritesInCache(recipeId: Int, isFavourite: Boolean) {
        withContext(Dispatchers.IO) {
            db.recipesDao().updateFavourites(recipeId, isFavourite)
        }
    }

    suspend fun getFavouriteStatus(recipeId: Int): Boolean {
        return withContext(Dispatchers.IO) {
            db.recipesDao().getFavouriteStatus(recipeId)
        }
    }
}