package com.maslinka.recipes.di

import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.ui.recipes.recipe.RecipeViewModel

class RecipeViewModelFactory(private val repository: RecipesRepository):Factory<RecipeViewModel> {
    override fun create(): RecipeViewModel {
        return RecipeViewModel(repository)
    }
}