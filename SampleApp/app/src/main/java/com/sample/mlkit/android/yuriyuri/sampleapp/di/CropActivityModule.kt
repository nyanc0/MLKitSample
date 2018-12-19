package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.support.v7.app.AppCompatActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.CropActivity
import dagger.Binds
import dagger.Module

@Module
interface CropActivityModule {
    @Binds
    fun providesAppCompatActivity(cropActivity: CropActivity): AppCompatActivity
}