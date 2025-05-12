package com.maslinka.recipes.ui

import android.os.Bundle
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

//        //проверка, что активность новая (только что созданная), а не восстановленная (как, например, при повороте)
//        //если активность новая, то добавляем фрагмент, а если не новая, значит фрагмент уже добавлен
//        if (savedInstanceState == null){
//            supportFragmentManager.commit {
//                add<CategoriesListFragment>(binding.fragmentContainerView.id)
//                setReorderingAllowed(true)
//            }
//        }


        binding.btnCategory.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.categoriesListFragment)
//            supportFragmentManager.commit {
//                replace<CategoriesListFragment>(binding.fragmentContainerView.id)
//                setReorderingAllowed(true)
//                addToBackStack(null)
//            }
        }

        binding.btnFavourites.setOnClickListener {
            findNavController(R.id.nav_host_fragment).navigate(R.id.favouritesFragment)
//            supportFragmentManager.commit {
//                replace<FavouritesFragment>(binding.fragmentContainerView.id)
//                setReorderingAllowed(true)
//                addToBackStack(null)
//            }
        }
    }
}
