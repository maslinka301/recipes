package com.maslinka.recipes.ui.recipes.recipe


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SeekBar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.divider.MaterialDividerItemDecoration
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.FragmentRecipeBinding
import com.maslinka.recipes.model.Ingredient
import com.maslinka.recipes.ui.Constants.ARG_RECIPE_ID


class RecipeFragment : Fragment() {
    private var _binding: FragmentRecipeBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private val recipeViewModel: RecipeViewModel by viewModels()

    private  var ingredientsAdapter = IngredientsAdapter(emptyList())
    private  var methodAdapter = MethodAdapter(emptyList())

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
        val recipeId =
            arguments?.getInt(ARG_RECIPE_ID) ?: throw IllegalStateException("Arguments are null")
        initUI(recipeId)
    }

    private fun initUI(recipeId: Int) {
        loadRecipeToVM(recipeId)
        setupObserver()
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
    }

    private fun updateRecipeInfo(state: RecipeViewModel.RecipeState) {
        with(binding) {
            tvRecipeListHeaderTitle.text = state.recipe?.title ?: ""
            ivRecipeListHeaderImage.contentDescription =
                String.format(
                    getString(R.string.content_description_recipe_item),
                    state.recipe?.title
                )
            ivRecipeListHeaderImage.setImageDrawable(
                state.recipeDrawable
            )
        }
    }

    private fun updateServings(state: RecipeViewModel.RecipeState) {
        with(binding) {
            tvServings.text =
                String.format(getString(R.string.number_of_servings), state.numberOfServings)
            (rvIngredients.adapter as IngredientsAdapter).updateIngredients(state.numberOfServings)
        }
    }

    private fun updateIconHeartImage(state: RecipeViewModel.RecipeState) {
        binding.ibIconHeart.setImageResource(
            if (state.isFavourite) R.drawable.ic_heart
            else R.drawable.ic_favourites
        )
    }

    private fun updateRecycler(state: RecipeViewModel.RecipeState) {
        state.recipe?.let { initRecycler(it.ingredients, it.method) }
        setupSeekBar()
    }


    private fun setIconHeartListener(recipeId: Int) {
        binding.ibIconHeart.setOnClickListener {
            recipeViewModel.onFavoritesClicked(recipeId)
        }
    }


//    private fun setupRecyclerViews() {
//        val divider = MaterialDividerItemDecoration(
//            requireContext(),
//            LinearLayoutManager.VERTICAL
//        ).apply {
//            isLastItemDecorated = false
//            dividerInsetStart = resources.getDimensionPixelSize(R.dimen._12dp)
//            dividerInsetEnd = resources.getDimensionPixelSize(R.dimen._12dp)
//        }
//
//        with(binding) {
//            rvIngredients.apply {
//                adapter = ingredientsAdapter
//                addItemDecoration(divider)
//            }
//            rvMethod.apply {
//                adapter = methodAdapter
//                addItemDecoration(divider)
//            }
//        }
//    }

    private fun setupSeekBar() {
        binding.sbServingsNumber.apply {
            setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
                override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
                    if (fromUser) {
                        recipeViewModel.updateServings(progress)
                    }
                }
                override fun onStartTrackingTouch(seekBar: SeekBar?) {}
                override fun onStopTrackingTouch(seekBar: SeekBar?) {}
            })
        }
    }


    private fun initRecycler(
        ingredientList: List<Ingredient> = emptyList(),
        methodList: List<String> = emptyList()
    ) {
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
        val ingredientsAdapter = IngredientsAdapter(ingredientList)
        val methodAdapter = MethodAdapter(methodList)

        with(binding) {
            rvIngredients.apply {
                adapter = ingredientsAdapter
                addItemDecoration(dividerItemDecoration)
            }
            rvMethod.apply {
                adapter = methodAdapter
                addItemDecoration(dividerItemDecoration)
            }
        }

//        binding.sbServingsNumber.setOnSeekBarChangeListener(object :
//            SeekBar.OnSeekBarChangeListener {
//            override fun onProgressChanged(p0: SeekBar?, p1: Int, p2: Boolean) {
//                recipeViewModel.updateServings(p1)
//            }
//
//            override fun onStartTrackingTouch(p0: SeekBar?) {
//                Log.d("!!!", "Started tracking touch")
//            }
//
//            override fun onStopTrackingTouch(p0: SeekBar?) {
//                Log.d("!!!", "Stopped tracking touch")
//            }
//
//        })
    }

    override fun onDestroyView() {
        _binding = null
        super.onDestroyView()
    }
}