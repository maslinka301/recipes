package com.maslinka.recipes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.add
import androidx.fragment.app.commit
import androidx.fragment.app.replace
import com.maslinka.recipes.databinding.ActivityMainBinding
import com.maslinka.recipes.CategoriesListFragment as CategoriesListFragment

class MainActivity : AppCompatActivity() {

    //биндинг будет инициализирован с помощью inflate() только при первом обращении к нему
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //проверка, что активность новая (только что созданная), а не восстановленная (как, например, при повороте)
        //если активность новая, то добавляем фрагмент, а если не новая, значит фрагмент уже добавлен
        if (savedInstanceState == null){
            supportFragmentManager.commit {
                add<CategoriesListFragment>(binding.fragmentContainerView.id)
                setReorderingAllowed(true)
            }
        }


        binding.btnCategory.setOnClickListener {
            supportFragmentManager.commit {
                replace<CategoriesListFragment>(binding.fragmentContainerView.id)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }

        binding.btnFavourites.setOnClickListener {
            supportFragmentManager.commit {
                replace<FavouritesFragment>(binding.fragmentContainerView.id)
                setReorderingAllowed(true)
                addToBackStack(null)
            }
        }
    }
}
