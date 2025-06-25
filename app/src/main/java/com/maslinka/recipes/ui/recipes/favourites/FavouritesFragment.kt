package com.maslinka.recipes.ui.recipes.favourites

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentFavouritesBinding
import com.maslinka.recipes.ui.categories.RecyclerViewsAdapter

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
        binding.ivFavouriteFragmentHeader.setImageResource(state.headerImageUrl)
        binding.ivFavouriteFragmentHeader.contentDescription = state.contentDescription.toString()
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
            binding.rvFavourites.visibility = View.GONE
            binding.tvFavouriteListIsEmpty.visibility = View.VISIBLE
        } else {
            binding.rvFavourites.visibility = View.VISIBLE
            binding.tvFavouriteListIsEmpty.visibility = View.GONE
            favouritesAdapter.dataSet = state.favouritesList
        }
    }

    fun openRecipeByRecipeId(recipeId: Int) {
        findNavController().navigate(
            FavouritesFragmentDirections.actionFavouritesFragmentToRecipeFragment(
                recipeId
            )
        )
    }

}