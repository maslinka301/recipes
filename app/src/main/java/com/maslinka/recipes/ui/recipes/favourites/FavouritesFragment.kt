package com.maslinka.recipes.ui.recipes.favourites

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
import com.maslinka.recipes.ui.AccessToPreferences.getFavourites
import com.maslinka.recipes.ui.Constants.ARG_RECIPE
import com.maslinka.recipes.R
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.data.STUB.getRecipesByIds
import com.maslinka.recipes.databinding.FragmentFavouritesBinding
import com.maslinka.recipes.ui.categories.RecyclerViewsAdapter
import com.maslinka.recipes.ui.recipes.recipe.RecipeFragment
import java.io.IOException

class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        initUI()
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initUI(){
        binding.ivFavouriteFragmentHeader.setImageDrawable(getImageFromAssets()?: resources.getDrawable(R.drawable.bcg_categories))
        binding.ivFavouriteFragmentHeader.contentDescription = R.string.content_description_favourites_fragment.toString()
    }

    private fun initRecycler() {
        val set = getFavourites(requireContext())
        if(set.isEmpty()){
            binding.rvFavourites.visibility = View.GONE
            binding.tvFavouriteListIsEmpty.visibility = View.VISIBLE
        }
        else{
            val favouriteRecipeList = getRecipesByIds(set)
            val adapter = RecyclerViewsAdapter(favouriteRecipeList)
            binding.rvFavourites.adapter = adapter

            adapter.setOnItemClickListener(object : RecyclerViewsAdapter.OnItemClickListener{
                override fun onItemClick(itemId: Int) {
                    openRecipeByRecipeId(itemId)
                }
            })
        }

    }

    private fun openRecipeByRecipeId(recipeId: Int) {
        val currRecipe = STUB.getRecipeById(recipeId)
        val bundle = bundleOf(ARG_RECIPE to currRecipe)
        parentFragmentManager.commit {
            replace<RecipeFragment>(R.id.fragmentContainerView, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }

    private fun getImageFromAssets(): Drawable? {
        val drawable =
            try {
                Drawable.createFromStream(requireContext().assets.open("bcg_favorites.png"), null)
            } catch (e: IOException) {
                Log.e("!!!", "Error loading image from assets", e)
                e.printStackTrace()
                null
            }
        return drawable
    }
}