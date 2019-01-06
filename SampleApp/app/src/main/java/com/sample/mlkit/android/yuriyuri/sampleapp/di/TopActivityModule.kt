package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.arch.lifecycle.ViewModel
import android.support.v7.app.AppCompatActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top.TopActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top.TopActivityViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
interface TopActivityModule {
    @Binds
    fun providesAppCompatActivity(topActivity: TopActivity): AppCompatActivity

    @Binds
    @IntoMap
    @ViewModelKey(TopActivityViewModel::class)
    fun bindTopActivityViewModel(topActivityViewModel: TopActivityViewModel): ViewModel
}