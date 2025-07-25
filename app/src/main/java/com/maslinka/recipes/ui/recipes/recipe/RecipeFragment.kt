package com.maslinka.recipes.ui.recipes.recipe


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import android.widget.SeekBar.OnSeekBarChangeListener
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.maslinka.recipes.R
import com.maslinka.recipes.RecipeApplication
import com.maslinka.recipes.databinding.FragmentRecipeBinding


class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private lateinit var recipeViewModel: RecipeViewModel

    private var ingredientsAdapter = IngredientsAdapter()
    private var methodAdapter = MethodAdapter()

    private val recipeFragmentArgs: RecipeFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val appContainer = (requireContext().applicationContext as RecipeApplication).appContainer
        recipeViewModel = appContainer.recipeViewModel.create()

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentRecipeBinding.inflate(inflater, container, false)
        return binding.root
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBundleData()
    }

    private fun initBundleData() {
        initUI(recipeFragmentArgs.recipeId)
    }

    private fun initUI(recipeId: Int) {
        setupObserver()
        loadRecipeToVM(recipeId)
        initRecyclerAdapters()
        createRecyclerItemDecorator()
        setIconHeartListener(recipeId)
    }

    private fun loadRecipeToVM(recipeId: Int) {
        recipeViewModel.loadRecipe(recipeId)
    }

    private fun setupObserver() {
        recipeViewModel.recipeState.observe(viewLifecycleOwner) { state ->
            updateRecycler(state)
            updateRecipeInfo(state)
            updateServings(state)
            updateIconHeartImage(state)
        }

        recipeViewModel.showToast.observe(viewLifecycleOwner) { state ->
            state.getContentIfNotHandled()?.let { resId ->
                Toast.makeText(requireContext(), getString(resId), Toast.LENGTH_SHORT).show()
            }

        }
    }

    private fun updateRecipeInfo(state: RecipeViewModel.RecipeState) {
        with(binding) {
            tvRecipeListHeaderTitle.text = state.recipe?.title ?: ""
            ivRecipeListHeaderImage.contentDescription =
                String.format(
                    getString(R.string.content_description_recipe_item),
                    state.recipe?.title
                )
            setImage(state)
        }
    }

    private fun setImage(state: RecipeViewModel.RecipeState) {
        Glide
            .with(binding.ivRecipeListHeaderImage.context)
            .load(state.recipeImageUrl)
            .placeholder(R.drawable.img_placeholder)
            .error(R.drawable.img_error)
            .into(binding.ivRecipeListHeaderImage)
    }

    private fun updateServings(state: RecipeViewModel.RecipeState) {
        with(binding) {
            tvServings.text =
                String.format(getString(R.string.number_of_servings), state.numberOfServings)
            (rvIngredients.adapter as? IngredientsAdapter)?.updateIngredients(state.numberOfServings)
        }
    }

    private fun updateIconHeartImage(state: RecipeViewModel.RecipeState) {
        binding.ibIconHeart.setImageResource(
            if (state.isFavourite) R.drawable.ic_heart
            else R.drawable.ic_favourites
        )
    }

    private fun updateRecycler(state: RecipeViewModel.RecipeState) {
        state.recipe?.let {
            ingredientsAdapter.dataSet = it.ingredients
            methodAdapter.dataSet = it.method
        }
        setupSeekBar()
    }


    private fun setIconHeartListener(recipeId: Int) {
        binding.ibIconHeart.setOnClickListener {
            recipeViewModel.onFavoritesClicked(recipeId)
        }
    }


    private fun setupSeekBar() {
        binding.sbServingsNumber.setOnSeekBarChangeListener(PortionSeekBarListener { progress ->
            recipeViewModel.updateServings(progress)
        })
    }


    private fun initRecyclerAdapters() {
        with(binding) {
            rvIngredients.adapter = ingredientsAdapter
            rvMethod.adapter = methodAdapter
        }
    }

    private fun createRecyclerItemDecorator() {
        val dividerItemDecoration = MaterialDividerItemDecoration(
            binding.rvIngredients.context,
            LinearLayoutManager.VERTICAL
        )
        val sizeInDp = resources.getDimensionPixelSize(R.dimen._12dp)
        dividerItemDecoration.apply {
            isLastItemDecorated = false
            dividerInsetStart = sizeInDp
            dividerInsetEnd = sizeInDp
        }
        with(binding) {
            rvIngredients.addItemDecoration(dividerItemDecoration)
            rvMethod.addItemDecoration(dividerItemDecoration)
        }
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}

class PortionSeekBarListener(val onChangeIngredients: (Int) -> Unit) : OnSeekBarChangeListener {
    override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
        if (p2) {
            onChangeIngredients(p1)
        }
    }

    override fun onStartTrackingTouch(p0: SeekBar?) {}

    override fun onStopTrackingTouch(p0: SeekBar?) {}

}