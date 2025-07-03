package com.maslinka.recipes.model

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Entity
@Parcelize
@Serializable
data class Category(
    @PrimaryKey val id: Int,
    @ColumnInfo (name = "category_title") val title: String,
    @ColumnInfo (name = "category_description") val description: String,
    @ColumnInfo (name = "category_imageUrl") val imageUrl: String,
):Parcelable