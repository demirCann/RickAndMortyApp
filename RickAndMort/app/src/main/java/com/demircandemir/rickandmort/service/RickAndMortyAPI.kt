package com.demircandemir.rickandmort.service

import com.demircandemir.rickandmort.model.CharacterList
import com.demircandemir.rickandmort.model.CharacterListItem
import com.demircandemir.rickandmort.model.LocationList
import com.demircandemir.rickandmort.model.Result
import com.demircandemir.rickandmort.util.Constants
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RickAndMortyAPI {

    @GET(Constants.END_POINT_LOCATION)
    suspend fun getLocationList(
        ) : Response<LocationList>


    @GET("character/{ids}")
    suspend fun getCharacterList(@Path("ids")  ids : String) : CharacterList

    @GET("character/{id}")
    suspend fun getCharacter(
        @Path("id")  id : String
    ) : CharacterListItem

}