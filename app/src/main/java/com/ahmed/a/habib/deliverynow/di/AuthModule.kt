package com.ahmed.a.habib.deliverynow.di

import android.content.Context
import com.ahmed.a.habib.deliverynow.dataStore.DeliveryNowDataStoreImp
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AuthModule {

    @Provides
    @Singleton
    fun provideDataStoreImplInstance(context: Context): DeliveryNowDataStoreImp {
        return DeliveryNowDataStoreImp(context)
    }
}