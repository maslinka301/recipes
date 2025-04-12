package com.maslinka.recipes.ui.recipes.recipe

import android.graphics.drawable.Drawable
import android.os.Build
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
import com.maslinka.recipes.ui.Constants.ARG_RECIPE
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentRecipeBinding
import com.maslinka.recipes.model.Recipe
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

        recipeViewModel.recipeState.observe(viewLifecycleOwner){ state ->
            Log.i("!!!", "Избранное: ${state.isFavourite}")
        }
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
        recipeViewModel.loadRecipe(recipe.id)
        binding.ivRecipeListHeaderImage.setImageDrawable(getImageFromAssets(recipe.imageUrl))
        binding.ivRecipeListHeaderImage.contentDescription =
            String.format(getString(R.string.content_description_recipe_item), recipe.title)
        //binding.tvRecipeListHeaderTitle.text = recipe.title
        //binding.tvServings.text = String.format(getString(R.string.number_of_servings), 1)
        initRecycler(recipe)

        recipeViewModel.recipeState.observe(viewLifecycleOwner){ state ->
            binding.tvRecipeListHeaderTitle.text = state.recipe?.title ?: ""
            binding.tvServings.text = String.format(getString(R.string.number_of_servings), state.numberOfServings)
            if (state.isFavourite){
                binding.ibIconHeart.setImageResource(R.drawable.ic_favourites)
            }
            else{
                binding.ibIconHeart.setImageResource(R.drawable.ic_heart)
            }
        }

        binding.ibIconHeart.setOnClickListener {
            recipeViewModel.onFavoritesClicked(recipe.id)
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
                ingredientsAdapter.updateIngredients(p1)
                binding.tvServings.text = String.format(getString(R.string.number_of_servings), p1)
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

    private fun getImageFromAssets(imageUrl: String): Drawable? {
        val drawable =
            try {
                Drawable.createFromStream(requireContext().assets.open(imageUrl), null)
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }
}