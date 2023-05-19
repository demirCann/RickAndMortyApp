package com.demircandemir.rickandmort.dependencyinjection

import com.demircandemir.rickandmort.repository.RickAndMortRepo
import com.demircandemir.rickandmort.service.RickAndMortyAPI
import com.demircandemir.rickandmort.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.create
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideRickAndMortyRepo(
        api : RickAndMortyAPI
    ) = RickAndMortRepo(api)



    @Singleton
    @Provides
    fun provideRickAndMortyApi() : RickAndMortyAPI {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(RickAndMortyAPI::class.java)
    }
}