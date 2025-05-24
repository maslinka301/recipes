package com.maslinka.recipes.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.maslinka.recipes.data.STUB
import com.maslinka.recipes.databinding.FragmentListCategoriesBinding
import com.maslinka.recipes.model.Category

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val categoriesListAdapter = RecyclerViewsAdapter()

    private val categoriesListViewModel: CategoriesListViewModel by viewModels()


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
        setupObservers()
        categoriesListViewModel.loadCategories()
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initRecycler() {
        binding.rvCategories.adapter = categoriesListAdapter
        Log.d("!!!", "Инициализация адаптера")
        categoriesListAdapter.setOnItemClickListener(object :
            RecyclerViewsAdapter.OnItemClickListener {
            override fun onItemClick(itemId: Int) {
                categoriesListViewModel.prepareNavigation(itemId)
            }
        })
    }

    private fun setupObservers() {
        categoriesListViewModel.categoryListState.observe(viewLifecycleOwner) { state ->
            updateRecycler(state)
            state.navigationData?.let { navigateToRecipesList(it) }
        }
    }

    private fun updateRecycler(state: CategoriesListViewModel.CategoriesListState) {
        categoriesListAdapter.dataSet = state.categoriesList

    }

    private fun navigateToRecipesList(category: Category) {
        if( category in STUB.getCategories())
            findNavController().navigate(CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(category))
        else
            throw IllegalStateException("Category not found")
    }
}