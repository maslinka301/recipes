package com.maslinka.recipes

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.maslinka.recipes.Constants.ARG_RECIPE

import com.maslinka.recipes.databinding.FragmentRecipeBinding
import kotlinx.coroutines.currentCoroutineContext

class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundleData()
    }

    private fun initBundleData() {
        arguments?.let { arguments ->
            val currRecipe: Recipe = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                arguments.getParcelable(ARG_RECIPE, Recipe::class.java)
                    ?: throw IllegalArgumentException("Recipe not found in arguments")
            } else {
                arguments.getParcelable(ARG_RECIPE)
                    ?: throw IllegalArgumentException("Recipe not found in arguments")
            }
            initUI(currRecipe)
        } ?: throw IllegalStateException("Arguments are null")
        //Пыталась сделать через приведение типов, но компилятор ругался на getParcelable(ARG_RECIPE)
//        val currRecipe : Recipe= if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
//            arguments?.getParcelable(ARG_RECIPE, Recipe::class.java) as Recipe
//        } else {
//            arguments?.getParcelable(ARG_RECIPE) as Recipe
//        }
    }

    private fun initUI(recipe: Recipe) {
        binding.tvRecipeTitle.text = recipe.title
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}