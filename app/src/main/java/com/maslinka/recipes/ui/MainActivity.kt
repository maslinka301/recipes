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

class MainActivity : AppCompatActivity() {

    //биндинг будет инициализирован с помощью inflate() только при первом обращении к нему
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("!!!", "Метод onCreate выполняется на потоке ${Thread.currentThread().name}")

        val thread = Thread{
            val url = URL("https://recipes.androidsprint.ru/api/category")
            val connection = url.openConnection() as HttpURLConnection
            connection.connect()
            val json = connection.inputStream.bufferedReader().readText()
            val categories = Json.decodeFromString<List<Category>>(json)

            Log.d("!!!", "responseCode: ${connection.responseCode}")
            Log.d("!!!", "responseMessage: ${connection.responseMessage}")
            Log.d("!!!", "body: $json")
            Log.d("!!!", "Выполняется запрос на потоке ${Thread.currentThread().name}")
            Log.d("!!!", "Список категорий $categories")
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
