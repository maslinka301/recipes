package com.maslinka.recipes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    //биндинг будет инициализирован с помощью inflate() только при первом обращении к нему
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("!!!", "Метод onCreate выполняется на потоке ${Thread.currentThread().name}")


        binding.btnCategory.setOnClickListener {
            val currDestination = findNavController(R.id.nav_host_fragment).currentDestination?.id
            if(currDestination != R.id.categoriesListFragment)
                findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
        }

        binding.btnFavourites.setOnClickListener {
            val currDestination = findNavController(R.id.nav_host_fragment).currentDestination?.id
            if(currDestination != R.id.favouritesFragment)
                findNavController(R.id.nav_host_fragment).navigate(R.id.favouritesFragment)
        }
    }
}
