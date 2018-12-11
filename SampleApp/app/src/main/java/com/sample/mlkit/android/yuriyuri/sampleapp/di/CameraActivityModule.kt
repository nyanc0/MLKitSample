package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.support.v7.app.AppCompatActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.CameraActivity
import dagger.Binds
import dagger.Module

@Module
interface CameraActivityModule {
    @Binds
    fun providesAppCompatActivity(cameraActivity: CameraActivity): AppCompatActivity
}