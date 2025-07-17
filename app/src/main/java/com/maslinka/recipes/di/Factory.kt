package com.maslinka.recipes.di

interface Factory<T> {
    fun create(): T
}