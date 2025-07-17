package com.maslinka.recipes.di

import com.maslinka.recipes.data.RecipesRepository
import com.maslinka.recipes.ui.recipes.recipeList.RecipeListViewModel

class RecipesListViewModelFactory(private val repository: RecipesRepository) :
    Factory<RecipeListViewModel> {
    override fun create(): RecipeListViewModel {
        return RecipeListViewModel(repository)
    }
}