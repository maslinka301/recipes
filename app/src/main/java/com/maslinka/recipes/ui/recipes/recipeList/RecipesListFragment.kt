package com.maslinka.recipes.ui.recipes.recipeList

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.bumptech.glide.Glide
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentListRecipesBinding
import com.maslinka.recipes.ui.categories.RecyclerViewsAdapter
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RecipesListFragment : Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val recipeListViewModel: RecipeListViewModel by viewModels()

    private val recipeListAdapter = RecyclerViewsAdapter()

    private val recipesListFragmentArgs: RecipesListFragmentArgs by navArgs()


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
        val category = recipesListFragmentArgs.category
        recipeListViewModel.initState(category.id, category.title, category.imageUrl)
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
        recipeListViewModel.showToast.observe(viewLifecycleOwner) { state ->
            state.getContentIfNotHandled()?.let{ resId ->
                Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun updateRecycler(state: RecipeListViewModel.RecipeListState) {
        recipeListAdapter.dataSet = state.recipeList
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            RecipesListFragmentDirections.actionRecipesListFragmentToRecipeFragment(
                recipeId
            )
        )
    }

    private fun updateUIInfo(state: RecipeListViewModel.RecipeListState) {
        with(binding) {
            tvRecipeListHeaderTitle.text = state.categoryName
            setImage(state)
        }
    }

    private fun setImage(state: RecipeListViewModel.RecipeListState) {
        Glide
            .with(binding.ivRecipeListHeaderImage.context)
            .load(state.categoryImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivRecipeListHeaderImage)
    }


}