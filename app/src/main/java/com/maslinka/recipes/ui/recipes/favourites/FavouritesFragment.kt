package com.maslinka.recipes.ui.recipes.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import androidx.fragment.app.viewModels
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentFavouritesBinding
import com.maslinka.recipes.ui.Constants.ARG_RECIPE_ID
import com.maslinka.recipes.ui.categories.RecyclerViewsAdapter
import com.maslinka.recipes.ui.recipes.recipe.RecipeFragment


class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val favouritesAdapter = RecyclerViewsAdapter()

    private val favouritesViewModel: FavouritesViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        favouritesViewModel.initState()
        initUI()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI() {
        setupObservers()
        initRecyclerAdapter()
        favouritesViewModel.updateFavouritesList()
    }

    private fun setupObservers() {
        favouritesViewModel.favouritesState.observe(viewLifecycleOwner) { state ->
            updateUI(state)
            updateRecycler(state)
        }
    }

    private fun updateUI(state: FavouritesViewModel.FavouritesState) {
        with(binding) {
            ivFavouriteFragmentHeader.setImageDrawable(state.headerImage)
            ivFavouriteFragmentHeader.contentDescription = state.contentDescription.toString()
        }

    }

    private fun initRecyclerAdapter() {
        binding.rvFavourites.adapter = favouritesAdapter
        favouritesAdapter.setOnItemClickListener(object :
            RecyclerViewsAdapter.OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipeByRecipeId(itemId)
            }
        })
    }

    private fun updateRecycler(state: FavouritesViewModel.FavouritesState) {
        if (state.listIsEmpty) {
            with(binding) {
                rvFavourites.visibility = View.GONE
                tvFavouriteListIsEmpty.visibility = View.VISIBLE
            }
        } else {
            favouritesAdapter.dataSet = state.favouritesList
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        val bundle = bundleOf(ARG_RECIPE_ID to recipeId)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.fragmentContainerView, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

}