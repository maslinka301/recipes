package com.maslinka.recipes.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_ID
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_IMAGE_URL
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_NAME
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentListRecipesBinding
import com.maslinka.recipes.ui.Constants.ARG_RECIPE_ID
import com.maslinka.recipes.ui.categories.RecyclerViewsAdapter

class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val recipeListViewModel: RecipeListViewModel by viewModels()

    private val recipeListAdapter = RecyclerViewsAdapter()


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
        initUI()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initBundleData() {
        arguments?.let { args ->
            val categoryId = args.getInt(ARG_CATEGORY_ID)
            val categoryName = args.getString(ARG_CATEGORY_NAME)
            val categoryImageUrl = args.getString(ARG_CATEGORY_IMAGE_URL)
            recipeListViewModel.initState(categoryId, categoryName, categoryImageUrl)
        } ?: throw IllegalStateException("Arguments are null")
    }

    private fun initUI() {
        initRecyclerAdapter()
        setupObservers()
    }

    private fun initRecyclerAdapter() {
        binding.rvRecipes.adapter = recipeListAdapter
        recipeListAdapter.setOnItemClickListener(object : RecyclerViewsAdapter.OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipeByRecipeId(itemId)
            }
        })
    }

    private fun setupObservers() {
        recipeListViewModel.recipeListState.observe(viewLifecycleOwner) { state ->
            updateUIInfo(state)
            updateRecycler(state)
        }
    }

    private fun updateRecycler(state: RecipeListViewModel.RecipeListState) {
        recipeListAdapter.dataSet = state.recipeList
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(recipeId))
    }

    private fun updateUIInfo(state: RecipeListViewModel.RecipeListState) {
        with(binding) {
            tvRecipeListHeaderTitle.text = state.categoryName
            ivRecipeListHeaderImage.setImageDrawable(state.categoryImage)
        }
    }

}