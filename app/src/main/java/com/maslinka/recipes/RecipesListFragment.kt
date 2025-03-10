package com.maslinka.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maslinka.recipes.databinding.FragmentListRecipesBinding

class RecipesListFragment: Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    var categoryId:Int? = null
    var categoryName:String? = null
    var categoryImageUrl:String? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListRecipesBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = requireArguments().getInt("ARG_CATEGORY_ID")
        categoryImageUrl = requireArguments().getString("ARG_CATEGORY_NAME")
        categoryName = requireArguments().getString("ARG_CATEGORY_IMAGE_URL")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}