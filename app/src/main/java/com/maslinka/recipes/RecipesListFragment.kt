package com.maslinka.recipes

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maslinka.recipes.Constants.ARG_CATEGORY_ID
import com.maslinka.recipes.Constants.ARG_CATEGORY_IMAGE_URL
import com.maslinka.recipes.Constants.ARG_CATEGORY_NAME
import com.maslinka.recipes.databinding.FragmentListRecipesBinding

class RecipesListFragment: Fragment() {
    private var _binding: FragmentListRecipesBinding? = null
    private val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    private var categoryId:Int? = null
    private var categoryName:String? = null
    private var categoryImageUrl:String? = null


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
        initBundleData()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun initBundleData(){
        arguments?.let { arguments ->
            categoryId = arguments.getInt(ARG_CATEGORY_ID)
            categoryName = arguments.getString(ARG_CATEGORY_NAME)
            categoryImageUrl = arguments.getString(ARG_CATEGORY_IMAGE_URL)
        }?: throw IllegalStateException("Arguments are null")
    }
}