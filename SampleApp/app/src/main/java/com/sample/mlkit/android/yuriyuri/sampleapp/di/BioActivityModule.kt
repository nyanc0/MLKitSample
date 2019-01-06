package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.support.v7.app.AppCompatActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio.BioActivity
import dagger.Binds
import dagger.Module

@Module
interface BioActivityModule {
    @Binds
    fun providesAppCompatActivity(activity: BioActivity): AppCompatActivity
}