package com.maslinka.recipes

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.maslinka.recipes.Constants.ARG_RECIPE
import com.maslinka.recipes.Constants.PREFERENCE_FILE
import com.maslinka.recipes.Constants.SAVED_FAVOURITES
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
        binding.tvServings.text = String.format(getString(R.string.number_of_servings), 1)
        initRecycler(recipe)

        //Работа с избранным
        val favouriteSet = getFavourites()
        if (recipe.id in favouriteSet) {
            binding.ibIconHeart.setImageResource(R.drawable.ic_heart)
        } else {
            binding.ibIconHeart.setImageResource(R.drawable.ic_favourites)
        }

        binding.ibIconHeart.setOnClickListener {
            if (recipe.id in favouriteSet) {
                favouriteSet.remove(recipe.id)
                binding.ibIconHeart.setImageResource(R.drawable.ic_favourites)
            } else {
                favouriteSet.add(recipe.id)
                binding.ibIconHeart.setImageResource(R.drawable.ic_heart)
            }
            saveFavourites(favouriteSet)
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

    private fun saveFavourites(ids: Set<Int>) {
        val sharedPrefs =
            activity?.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE) ?: return
        val strSet = ids.map { it.toString() }.toSet()
        with(sharedPrefs.edit()) {
            putStringSet(SAVED_FAVOURITES, strSet)
            apply()
        }
    }

    private fun getFavourites(): MutableSet<Int> {
        //activity?.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)?.edit()?.clear()?.apply()

        val sharedPref = activity?.getSharedPreferences(PREFERENCE_FILE, Context.MODE_PRIVATE)
            ?: return mutableSetOf()
        val favouritesSet =
            sharedPref.getStringSet(SAVED_FAVOURITES, mutableSetOf()) ?: return mutableSetOf()
        val intSet = favouritesSet.mapNotNull { it.toIntOrNull() }.toMutableSet() ?: mutableSetOf()
        return intSet
    }

}