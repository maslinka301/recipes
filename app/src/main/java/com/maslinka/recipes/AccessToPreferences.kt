package com.maslinka.recipes

import android.content.Context
import com.maslinka.recipes.Constants.PREFERENCE_FILE
import com.maslinka.recipes.Constants.SAVED_FAVOURITES

object AccessToPreferences {
    fun saveFavourites(context: Context, ids: Set<Int>) {
        val sharedPrefs =
            context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE) ?: return
        val strSet = ids.map { it.toString() }.toSet()
        with(sharedPrefs.edit()) {
            putStringSet(SAVED_FAVOURITES, strSet)
            apply()
        }
    }

    fun getFavourites(context: Context): MutableSet<Int> {
        //activity?.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)?.edit()?.clear()?.apply()

        val sharedPref = context.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
            ?: return mutableSetOf()
        val favouritesSet =
            sharedPref.getStringSet(SAVED_FAVOURITES, mutableSetOf()) ?: return mutableSetOf()
        val intSet = favouritesSet.mapNotNull { it.toIntOrNull() }.toMutableSet() ?: mutableSetOf()
        return intSet
    }
}