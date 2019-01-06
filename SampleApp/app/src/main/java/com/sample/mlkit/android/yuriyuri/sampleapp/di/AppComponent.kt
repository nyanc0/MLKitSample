package com.sample.mlkit.android.yuriyuri.sampleapp.di

import android.app.Application
import com.sample.mlkit.android.yuriyuri.sampleapp.MLKitApp
import dagger.BindsInstance
import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import javax.inject.Singleton

@Singleton
@Component(modules = [
    AndroidSupportInjectionModule::class,
    AppModule::class,
    ActivityModule::class,
    ViewModelModule::class])
interface AppComponent : AndroidInjector<MLKitApp> {

    @Component.Builder
    interface Builder {

        @BindsInstance
        fun application(application: Application): Builder

        fun build(): AppComponent
    }

    override fun inject(instance: MLKitApp)
}