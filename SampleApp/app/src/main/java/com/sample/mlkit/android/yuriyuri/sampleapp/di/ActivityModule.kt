package com.sample.mlkit.android.yuriyuri.sampleapp.di

import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.CameraActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.CropActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.MainActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.bio.BioActivity
import com.sample.mlkit.android.yuriyuri.sampleapp.presentation.top.TopActivity
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

    @ContributesAndroidInjector(modules = [
        CropActivityModule::class])
    fun contributeCropActivity(): CropActivity

    @ContributesAndroidInjector(modules = [
        TopActivityModule::class])
    fun contributeTopActivity(): TopActivity

    @ContributesAndroidInjector(modules = [
        BioActivityModule::class])
    fun contributeBioActivity(): BioActivity
}