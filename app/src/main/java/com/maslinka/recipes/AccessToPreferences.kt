package com.maslinka.recipes

import android.content.Context
import com.maslinka.recipes.Constants.PREFERENCE_FILE
import com.maslinka.recipes.Constants.SAVED_FAVOURITES

object AccessToPreferences {
    fun saveFavourites(context: Context, ids: Set<Int>) {
        val sharedPrefs =
            context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE) ?: return
        val strSet = ids.map { it.toString() }.toSet()
        sharedPrefs.edit().putStringSet(SAVED_FAVOURITES, strSet).apply()
    }

    fun getFavourites(context: Context): MutableSet<Int> {
        val sharedPref = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
            ?: return HashSet()

        val favouritesSet = sharedPref.getStringSet(SAVED_FAVOURITES, null)
            ?: return HashSet()

        return favouritesSet
            .mapNotNull { it.toIntOrNull() }
            .toCollection(HashSet())
    }
}