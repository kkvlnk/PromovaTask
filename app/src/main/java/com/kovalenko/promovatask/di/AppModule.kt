package com.kovalenko.promovatask.di

import org.koin.core.annotation.ComponentScan
import org.koin.core.annotation.Module

@Module(includes = [DatabaseModule::class])
@ComponentScan("com.kovalenko.promovatask")
class AppModule