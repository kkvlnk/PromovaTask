package com.kovalenko.promovatask

import android.app.Application
import com.kovalenko.promovatask.di.AppModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.ksp.generated.module

class PromovaTaskApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()
            androidContext(this@PromovaTaskApplication)
            modules(AppModule().module)
        }
    }
}