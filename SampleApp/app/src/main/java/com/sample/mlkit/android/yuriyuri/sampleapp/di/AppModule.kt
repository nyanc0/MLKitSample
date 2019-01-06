package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.app.Application
import android.content.Context
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.ImageCameraRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.ImageRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.data.repository.MenuRepository
import com.sample.mlkit.android.yuriyuri.sampleapp.util.AppSchedulerProvider
import com.sample.mlkit.android.yuriyuri.sampleapp.util.SchedulerProvider
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
internal object AppModule {

    @Singleton
    @Provides
    @JvmStatic
    fun provideContext(application: Application): Context = application

    @Singleton
    @Provides
    @JvmStatic
    fun provideSchedulerProvider(): SchedulerProvider = AppSchedulerProvider()

    @Singleton
    @Provides
    @JvmStatic
    fun provideImageRepository(schedulerProvider: SchedulerProvider): ImageRepository = ImageCameraRepository(schedulerProvider)

    @Singleton
    @Provides
    @JvmStatic
    fun provideMenuRepository(schedulerProvider: SchedulerProvider): MenuRepository = MenuRepository(schedulerProvider)
}