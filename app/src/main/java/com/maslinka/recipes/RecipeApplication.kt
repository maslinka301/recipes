package com.maslinka.recipes

import android.app.Application
import com.maslinka.recipes.di.AppContainer

class RecipeApplication() : Application() {
    lateinit var appContainer: AppContainer

    override fun onCreate() {
        super.onCreate()
        //до этого момента инициализировать не можем, тк контекста нет, а он нужен для БД
        appContainer = AppContainer(this)
    }


}