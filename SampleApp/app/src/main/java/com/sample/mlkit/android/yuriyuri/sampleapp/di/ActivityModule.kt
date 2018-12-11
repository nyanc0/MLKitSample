package com.sample.mlkit.android.yuriyuri.sampleapp.di

import com.sample.mlkit.android.yuriyuri.sampleapp.CameraActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.MainActivity
import dagger.Module
import dagger.android.ContributesAndroidInjector

@Module
interface ActivityModule {

    @ContributesAndroidInjector(modules = [
        MainActivityModule::class
    ])
    fun contributeMainActivity(): MainActivity

    @ContributesAndroidInjector(modules = [
        CameraActivityModule::class])
    fun contributeCameraactivity(): CameraActivity
}