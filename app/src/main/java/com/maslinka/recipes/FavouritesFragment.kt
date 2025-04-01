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
import com.maslinka.recipes.AccessToPreferences.getFavourites
import com.maslinka.recipes.Constants.ARG_RECIPE
import com.maslinka.recipes.STUB.getRecipesByIds
import com.maslinka.recipes.databinding.FragmentFavouritesBinding
import java.io.IOException
import java.io.InputStream

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
        binding.ivFavouriteFragmentHeader.setImageDrawable(getImageFromAssets())
    }

    private fun initRecycler() {
        val set = getFavourites(requireContext())
        val favouriteRecipeList = getRecipesByIds(set)
        val adapter = RecyclerViewsAdapter(favouriteRecipeList)
        binding.rvFavourites.adapter = adapter

//        adapter.setOnItemClickListener(object : RecyclerViewsAdapter.OnItemClickListener{
//            override fun onItemClick(itemId: Int) {
//                parentFragmentManager.commit {
//                    openRecipeByRecipeId(itemId)
//                }
//            }
//        })
    }

//    fun openRecipeByRecipeId(recipeId: Int) {
//        val currRecipe = STUB.getRecipeById(recipeId)
//        val bundle = bundleOf(ARG_RECIPE to currRecipe)
//        parentFragmentManager.commit {
//            replace<RecipeFragment>(R.id.fragmentContainerView, args = bundle)
//            setReorderingAllowed(true)
//            addToBackStack(null)
//        }
//    }

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