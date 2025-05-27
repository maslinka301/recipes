package com.maslinka.recipes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.ActivityMainBinding
import com.maslinka.recipes.model.Category
import kotlinx.serialization.json.Json
import java.net.HttpURLConnection
import java.net.URL
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    //биндинг будет инициализирован с помощью inflate() только при первом обращении к нему
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("!!!", "Метод onCreate выполняется на потоке ${Thread.currentThread().name}")

        val thread = Thread{
            val urlCategory = URL("https://recipes.androidsprint.ru/api/category")
            val connectionCategory = urlCategory.openConnection() as HttpURLConnection

            connectionCategory.connect()

            val jsonCategory = connectionCategory.inputStream.bufferedReader().readText()
            val categories = Json.decodeFromString<List<Category>>(jsonCategory)
            lateinit var jsonRecipesInCategory: String

            Log.d("!!!", "responseCode: ${connectionCategory.responseCode}")
            Log.d("!!!", "responseMessage: ${connectionCategory.responseMessage}")
            Log.d("!!!", "body: $jsonCategory")
            Log.d("!!!", "Выполняется запрос на потоке ${Thread.currentThread().name}")
            Log.d("!!!", "Список категорий $categories")

            val threadPool = Executors.newFixedThreadPool(10)
            for (category in categories){
                threadPool.execute {
                    val urlRecipesInCategory = URL("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                    val connectionRecipesInCategory = urlRecipesInCategory.openConnection() as HttpURLConnection
                    connectionRecipesInCategory.connect()
                    jsonRecipesInCategory = connectionRecipesInCategory.inputStream.bufferedReader().readText()
                    //Log.d("!!!", "${Json.decodeFromString<List<Recipe>>(jsonRecipesInCategory)}")
                    Log.d("!!!", jsonRecipesInCategory)
                }
            }
        }
        thread.start()


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
