package com.maslinka.recipes

import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.maslinka.recipes.Constants.ARG_RECIPE
import com.maslinka.recipes.databinding.FragmentRecipeBinding
import java.io.IOException


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
    }

    private fun initUI(recipe: Recipe) {
        binding.ivRecipeListHeaderImage.setImageDrawable(getImageFromAssets(recipe.imageUrl))
        binding.ivRecipeListHeaderImage.contentDescription =
            String.format(getString(R.string.content_description_recipe_item), recipe.title)
        binding.tvRecipeListHeaderTitle.text = recipe.title
        initRecycler(recipe)
    }

    private fun initRecycler(recipe: Recipe) {
        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.rvIngredients.context,
            LinearLayoutManager.VERTICAL
        )
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(dividerItemDecoration)
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(dividerItemDecoration)
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }

    private fun getImageFromAssets(imageUrl: String): Drawable? {
        val drawable =
            try {
                Drawable.createFromStream(binding.root.context.assets.open(imageUrl), null)
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }
}