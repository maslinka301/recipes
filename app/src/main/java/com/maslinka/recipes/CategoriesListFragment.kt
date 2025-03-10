package com.maslinka.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.maslinka.recipes.databinding.FragmentListCategoriesBinding

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

    private fun initRecycler() {
        val categoriesListAdapter = CategoriesListAdapter(STUB.getCategories())
        binding.rvCategories.adapter = categoriesListAdapter
        Log.d("!!!", "Инициализация адаптера")
        categoriesListAdapter.setOnItemClickListener(object :
            CategoriesListAdapter.OnItemClickListener {
            override fun onItemClick(categoryId: Int) {
                openRecipesByCategoryId(categoryId)
            }
        })
    }

    private fun openRecipesByCategoryId(categoryId: Int) {
        val categoryName = STUB.getCategory(categoryId).title
        val categoryImageUrl = STUB.getCategory(categoryId).imageUrl

        val bundle = bundleOf(
            "ARG_CATEGORY_ID" to categoryId,
            "ARG_CATEGORY_NAME" to categoryName,
            "ARG_CATEGORY_IMAGE_URL" to categoryImageUrl
        )
        parentFragmentManager.commit {
            replace<RecipesListFragment>(R.id.fragmentContainerView, args = bundle)
            setReorderingAllowed(true)
            addToBackStack(null)
        }
    }
}