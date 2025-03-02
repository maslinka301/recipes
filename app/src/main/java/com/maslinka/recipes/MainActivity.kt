package com.maslinka.recipes

import android.os.Bundle
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
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

        supportFragmentManager.commit {
            add<CategoriesListFragment>(binding.fragmentContainerView.id)
            setReorderingAllowed(true)
            addToBackStack(null)
        }

        binding.btnCategory.setOnClickListener {
            supportFragmentManager.commit {
                replace<CategoriesListFragment>(binding.fragmentContainerView.id)
                setReorderingAllowed(true)
                addToBackStack("name") //для возврата к предыдущему открытому
            }

        }

        binding.btnFavourites.setOnClickListener {
            supportFragmentManager.commit {
                replace<FavouritesFragment>(binding.fragmentContainerView.id)
                setReorderingAllowed(true)
                addToBackStack("name")
            }

        }
    }
}
