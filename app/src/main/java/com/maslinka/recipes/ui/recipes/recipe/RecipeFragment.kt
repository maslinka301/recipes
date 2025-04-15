package com.maslinka.recipes.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentRecipeBinding
import com.maslinka.recipes.model.Recipe
import com.maslinka.recipes.ui.Constants.ARG_RECIPE_ID
import java.io.IOException


class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val recipeViewModel: RecipeViewModel by viewModels()

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
        val recipeId = arguments?.getInt(ARG_RECIPE_ID) ?: throw IllegalStateException("Arguments are null")
        initUI(recipeId)
    }

    private fun initUI(recipeId: Int) {
        recipeViewModel.loadRecipe(recipeId)
        //binding.ivRecipeListHeaderImage.setImageDrawable(getImageFromAssets(recipeViewModel.recipeState.value?.recipe?.imageUrl ?: throw IllegalStateException("Image is not found")))
        binding.ivRecipeListHeaderImage.contentDescription =
            String.format(getString(R.string.content_description_recipe_item), recipeViewModel.recipeState.value?.recipe?.title)
        initRecycler(recipeViewModel.recipeState.value?.recipe ?: throw IllegalStateException("Recipe is null"))

        recipeViewModel.recipeState.observe(viewLifecycleOwner){ state ->
            binding.tvRecipeListHeaderTitle.text = state.recipe?.title ?: ""
            binding.tvServings.text = String.format(getString(R.string.number_of_servings), state.numberOfServings)
            (binding.rvIngredients.adapter as IngredientsAdapter).updateIngredients(state.numberOfServings)

            binding.ibIconHeart.setImageResource(
                if(state.isFavourite) R.drawable.ic_heart
                else R.drawable.ic_favourites
            )
            binding.ivRecipeListHeaderImage.setImageDrawable(state.recipeDrawable)

        }

        binding.ibIconHeart.setOnClickListener {
            recipeViewModel.onFavoritesClicked(recipeId)
        }
    }

    private fun initRecycler(recipe: Recipe) {
        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.rvIngredients.context,
            LinearLayoutManager.VERTICAL
        )
        val sizeInDp = resources.getDimensionPixelSize(R.dimen._12dp)
        dividerItemDecoration.apply {
            isLastItemDecorated = false
            dividerInsetStart = sizeInDp
            dividerInsetEnd = sizeInDp
        }
        val ingredientsAdapter = IngredientsAdapter(recipe.ingredients)
        val methodAdapter = MethodAdapter(recipe.method)
        binding.rvIngredients.adapter = ingredientsAdapter
        binding.rvIngredients.addItemDecoration(dividerItemDecoration)
        binding.rvMethod.adapter = methodAdapter
        binding.rvMethod.addItemDecoration(dividerItemDecoration)

        binding.sbServingsNumber.setOnSeekBarChangeListener(object :
            SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
                recipeViewModel.updateServings(p1)
            }

            override fun onStartTrackingTouch(p0: SeekBar?) {
                Log.d("!!!", "Started tracking touch")
            }

            override fun onStopTrackingTouch(p0: SeekBar?) {
                Log.d("!!!", "Stopped tracking touch")
            }

        })


    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}