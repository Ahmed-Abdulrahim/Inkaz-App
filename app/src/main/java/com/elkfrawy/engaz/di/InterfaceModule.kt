package com.elkfrawy.engaz.di

import android.provider.Telephony.Mms.Rate
import com.elkfrawy.engaz.data.local.datastore.LocalDataStore
import com.elkfrawy.engaz.data.local.datastore.LocalDataStoreImpl
import com.elkfrawy.engaz.data.remote.car.CarRemoteSource
import com.elkfrawy.engaz.data.remote.car.CarRemoteSourceImpl
import com.elkfrawy.engaz.data.remote.firebase.FirebaseRemoteSource
import com.elkfrawy.engaz.data.remote.firebase.FirebaseRemoteSourceImpl
import com.elkfrawy.engaz.data.remote.history.HistoryRemoteSource
import com.elkfrawy.engaz.data.remote.history.HistoryRemoteSourceImpl
import com.elkfrawy.engaz.data.remote.rate.RateRemoteSource
import com.elkfrawy.engaz.data.remote.rate.RateRemoteSourceImpl
import com.elkfrawy.engaz.data.remote.user.AuthRemoteSource
import com.elkfrawy.engaz.data.remote.user.AuthRemoteSourceImpl
import com.elkfrawy.engaz.data.repository.*
import com.elkfrawy.engaz.domain.location.LocationClient
import com.elkfrawy.engaz.domain.location.LocationClientImpl
import com.elkfrawy.engaz.domain.repository.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class InterfaceModule {

    @Singleton
    @Binds
    abstract fun provideLocationClient(LocationClient: LocationClientImpl): LocationClient

    @Singleton
    @Binds
    abstract fun provideDataStoreRepository(dataStoreRepository: DataStoreRepositoryImpl): DataStoreRepository

    @Singleton
    @Binds
    abstract fun provideLocalDataStore(localDataStore: LocalDataStoreImpl): LocalDataStore

    @Singleton
    @Binds
    abstract fun provideAuthRepo(authRepo: AuthRepositoryImpl): AuthRepository

    @Singleton
    @Binds
    abstract fun provideAuthRemote(authRemote: AuthRemoteSourceImpl): AuthRemoteSource

    @Singleton
    @Binds
    abstract fun provideCarRepo(carRepo: CarRepositoryImpl): CarRepository

    @Singleton
    @Binds
    abstract fun provideCarRemote(carRemote: CarRemoteSourceImpl): CarRemoteSource

    @Singleton
    @Binds
    abstract fun provideHistoryRepo(historyRepo: HistoryRepositoryImpl): HistoryRepository

    @Singleton
    @Binds
    abstract fun provideHistoryRemote(historyRemote: HistoryRemoteSourceImpl): HistoryRemoteSource

    @Singleton
    @Binds
    abstract fun provideRateRepo(rateRepo: RateRepositoryImpl): RateRepository

    @Singleton
    @Binds
    abstract fun provideRateRemote(rateRemoteSource: RateRemoteSourceImpl): RateRemoteSource

    @Singleton
    @Binds
    abstract fun provideFirebaseRepo(firebaseRepository: FirebaseRepositoryImpl): FirebaseRepository

    @Singleton
    @Binds
    abstract fun provideFirebaseRemote(firebaseRemote: FirebaseRemoteSourceImpl): FirebaseRemoteSource


}
