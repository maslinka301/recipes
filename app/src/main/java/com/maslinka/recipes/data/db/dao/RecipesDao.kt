package com.maslinka.recipes.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maslinka.recipes.model.Recipe

@Dao
interface RecipesDao {
    @Query("SELECT * FROM Recipe WHERE recipe_categoryId = :categoryId")
    fun getRecipesByCategoryId(categoryId: Int): List<Recipe>

    @Query("SELECT * FROM Recipe WHERE recipe_isFavourite = 1")
    fun getFavouritesRecipes():List<Recipe>

    @Query("UPDATE Recipe SET recipe_isFavourite = :isFavourite WHERE id = :recipeId")
    fun updateFavourites(recipeId: Int, isFavourite: Boolean)

    @Query("SELECT recipe_isFavourite FROM Recipe WHERE id = :recipeId")
    fun getFavouriteStatus(recipeId: Int): Boolean

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addRecipes(recipes: List<Recipe>)
}