package com.maslinka.recipes

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.maslinka.recipes.databinding.FragmentFavouritesBinding

class FavouritesFragment: Fragment() {

    var _binding:FragmentFavouritesBinding? = null
    val binding
        get() = _binding ?: throw IllegalStateException("binding не инициализировано")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentFavouritesBinding.inflate(inflater)
        return binding.root
    }
}