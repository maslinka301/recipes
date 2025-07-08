package com.maslinka.recipes.data

import androidx.room.TypeConverter
import com.maslinka.recipes.model.Ingredient
import kotlinx.serialization.json.Json

class Converters {
    @TypeConverter
    fun convertFromIngredients(ingredients: List<Ingredient>): String{
        return Json.encodeToString(ingredients)
    }

    @TypeConverter
    fun convertToIngredients(ingredients: String): List<Ingredient>{
        return Json.decodeFromString(ingredients)
    }

    @TypeConverter
    fun convertFromMethod(method: List<String>): String{
        return Json.encodeToString(method)
    }

    @TypeConverter
    fun convertToMethod(method: String): List<String>{
        return Json.decodeFromString(method)
    }
}