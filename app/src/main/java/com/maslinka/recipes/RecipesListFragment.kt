package com.maslinka.recipes

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.maslinka.recipes.Constants.ARG_CATEGORY_ID
import com.maslinka.recipes.Constants.ARG_CATEGORY_IMAGE_URL
import com.maslinka.recipes.Constants.ARG_CATEGORY_NAME
import com.maslinka.recipes.Constants.ARG_RECIPE_ID
import com.maslinka.recipes.databinding.FragmentListRecipesBinding
import java.io.IOException

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private var categoryId: Int? = null
    private var categoryName: String? = null
    private var categoryImageUrl: String? = null


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundleData()
        categoryId?.let { initRecycler(it) }
        binding.tvRecipeHeaderTitle.text = categoryName
        binding.ivRecipeHeaderImage.setImageDrawable(getCategoryImageFromAssets())
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        arguments?.let { arguments ->
            categoryId = arguments.getInt(ARG_CATEGORY_ID)
            categoryName = arguments.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = arguments.getString(ARG_CATEGORY_IMAGE_URL)
        } ?: throw IllegalStateException("Arguments are null")
    }

    private fun initRecycler(categoryId: Int) {
        val adapter = RecyclerViewsAdapter(STUB.getRecipesByCategoryId(categoryId))
        binding.rvRecipes.adapter = adapter
        adapter.setOnItemClickListener(object : RecyclerViewsAdapter.OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipeByRecipeId(itemId)
            }
        })
    }

    private fun getCategoryImageFromAssets(): Drawable? {
        val drawable =
            try {
                Drawable.createFromStream(
                    categoryImageUrl?.let { binding.root.context.assets.open(it) },
                    null
                )
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.fragmentContainerView)
            setReorderingAllowed(true)
            addToBackStack(null)
        }

    }
}