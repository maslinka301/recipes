package com.maslinka.recipes.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_ID
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_IMAGE_URL
import com.maslinka.recipes.ui.Constants.ARG_CATEGORY_NAME
import com.maslinka.recipes.R
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.databinding.FragmentListCategoriesBinding
import com.maslinka.recipes.ui.recipes.recipeList.RecipesListFragment

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListCategoriesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initRecycler()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //    private fun initRecycler2() {
//        val categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())
//        binding.rvCategories.adapter = categoriesListAdapter
//        Log.d("!!!", "Инициализация адаптера")
//        categoriesListAdapter.setOnItemClickListener(object :
//            CategoriesListAdapter.OnItemClickListener {
//            override fun onItemClick(categoryId: Int) {
//                openRecipesByCategoryId(categoryId)
//            }
//        })
//    }
    private fun initRecycler() {
        val categoriesListAdapter = RecyclerViewsAdapter(STUB.getCategories())
        binding.rvCategories.adapter = categoriesListAdapter
        Log.d("!!!", "Инициализация адаптера")
        categoriesListAdapter.setOnItemClickListener(object :
            RecyclerViewsAdapter.OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                openRecipesByCategoryId(itemId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val currCategory = STUB.getCategory(categoryId)
        val categoryName = currCategory.title
        val categoryImageUrl = currCategory.imageUrl

        val bundle = bundleOf(
            ARG_CATEGORY_ID to categoryId,
            ARG_CATEGORY_NAME to categoryName,
            ARG_CATEGORY_IMAGE_URL to categoryImageUrl
        )
        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.fragmentContainerView, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}