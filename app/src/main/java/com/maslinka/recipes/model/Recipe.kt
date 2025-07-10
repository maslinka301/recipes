package com.maslinka.recipes.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Entity
@Parcelize
@Serializable
data class Recipe(
    @PrimaryKey val id: Int,
    @ColumnInfo(name = "recipe_title") val title: String,
    @ColumnInfo(name = "recipe_ingredients") val ingredients: List<Ingredient>,
    @ColumnInfo(name = "recipe_method") val method: List<String>,
    @ColumnInfo(name = "recipe_imageUrl") val imageUrl: String,
    @Transient @ColumnInfo(name = "recipe_categoryId") val categoryId: Int? = null,
    @Transient @ColumnInfo(name = "recipe_isFavourite") val isFavorite: Boolean = false,
) : Parcelable