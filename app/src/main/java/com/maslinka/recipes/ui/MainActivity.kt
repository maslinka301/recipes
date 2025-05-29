package com.maslinka.recipes.ui

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.findNavController
import com.maslinka.recipes.R
import com.maslinka.recipes.databinding.ActivityMainBinding
import com.maslinka.recipes.model.Category
import kotlinx.serialization.json.Json
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.Executors

class MainActivity : AppCompatActivity() {

    //биндинг будет инициализирован с помощью inflate() только при первом обращении к нему
    private val binding by lazy { ActivityMainBinding.inflate(layoutInflater) }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        Log.d("!!!", "Метод onCreate выполняется на потоке ${Thread.currentThread().name}")



        val thread = Thread {
            val logging = HttpLoggingInterceptor { message ->
                Log.d("HTTP", message)
            }.apply {
                level = HttpLoggingInterceptor.Level.BODY // Полный лог: заголовки и тело
            }

            val client = OkHttpClient.Builder()
                .addInterceptor(logging)
                .build()

            val request = Request.Builder()
                .url("https://recipes.androidsprint.ru/api/category")
                .build()

            client.newCall(request).execute().use { response ->
                val jsonCategory = response.body?.string() ?: ""
                val categories = Json.decodeFromString<List<Category>>(jsonCategory)


                Log.d("!!!", "responseCode: ${response.code}")
                Log.d("!!!", "responseMessage: ${response.message}")
                Log.d("!!!", "body: $jsonCategory")
                Log.d("!!!", "Выполняется запрос на потоке ${Thread.currentThread().name}")
                Log.d("!!!", "Список категорий $categories")

                val threadPool = Executors.newFixedThreadPool(10)
                for (category in categories) {
                    threadPool.execute {
                        val innerRequest = Request.Builder()
                            .url("https://recipes.androidsprint.ru/api/category/${category.id}/recipes")
                            .build()
                        client.newCall(innerRequest).execute().use { innerResponse ->
                            val jsonRecipes = innerResponse.body?.string() ?: ""
                            Log.d("!!!", jsonRecipes)
                        }
                    }
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
