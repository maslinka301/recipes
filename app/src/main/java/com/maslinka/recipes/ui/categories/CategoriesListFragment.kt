package com.maslinka.recipes.ui.categories

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.maslinka.recipes.R
import com.maslinka.recipes.RecipeApplication
import com.maslinka.recipes.databinding.FragmentListCategoriesBinding
import com.maslinka.recipes.model.Category

class CategoriesListFragment : Fragment() {

    private var _binding: FragmentListCategoriesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val categoriesListAdapter = RecyclerViewsAdapter()

    private lateinit var categoriesListViewModel: CategoriesListViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireContext().applicationContext as RecipeApplication).appContainer
        categoriesListViewModel = CategoriesListViewModel(appContainer.repository)
    }


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
        categoriesListViewModel.loadCategoriesFromCache()
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
            state.navigationData?.let {
                navigateToRecipesList(it)
                categoriesListViewModel.navigationReset()
            }
            showNetworkErrorToast(state)
        }
    }

    private fun updateRecycler(state: CategoriesListViewModel.CategoriesListState) {
        categoriesListAdapter.dataSet = state.categoriesList
    }

    private fun navigateToRecipesList(category: Category) {
        findNavController().navigate(
            CategoriesListFragmentDirections.actionCategoriesListFragmentToRecipesListFragment(
                category
            )
        )
    }

    private fun showNetworkErrorToast(state: CategoriesListViewModel.CategoriesListState) {
        if (state.showNetworkError) {
            Toast.makeText(requireContext(), R.string.network_error, Toast.LENGTH_SHORT).show()
            categoriesListViewModel.resetError()
        }
    }
}