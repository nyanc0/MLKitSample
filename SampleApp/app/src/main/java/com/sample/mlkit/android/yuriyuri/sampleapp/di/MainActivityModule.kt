package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.support.v7.app.AppCompatActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.MainActivity
import dagger.Binds
import dagger.Module

@Module
interface MainActivityModule {

    @Binds
    fun providesAppCompatActivity(mainActivity: MainActivity): AppCompatActivity

    // TODO:add fragment injector
}