package com.maslinka.recipes.di

import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.ui.recipes.favourites.FavouritesViewModel

class FavoritesViewModelFactory(private val repository: RecipesRepository) :
    Factory<FavouritesViewModel> {
    override fun create(): FavouritesViewModel {
        return FavouritesViewModel(repository)
    }
}