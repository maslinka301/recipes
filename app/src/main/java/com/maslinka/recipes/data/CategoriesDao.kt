package com.maslinka.recipes.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.maslinka.recipes.model.Category

@Dao
interface CategoriesDao {
    @Query ("SELECT * FROM Category")
    fun getAllCategories(): List<Category>

    @Insert
    fun addCategory(category: Category)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addCategories(categories: List<Category>)
}