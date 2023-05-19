package com.demircandemir.rickandmort.repository

import com.demircandemir.rickandmort.model.CharacterList
import com.demircandemir.rickandmort.model.CharacterListItem
import com.demircandemir.rickandmort.model.LocationList
import com.demircandemir.rickandmort.model.Result
import com.demircandemir.rickandmort.service.RickAndMortyAPI
import com.demircandemir.rickandmort.util.Resource
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
class RickAndMortRepo @Inject constructor(private val api : RickAndMortyAPI) {

    suspend fun getLocations() : Resource<List<Result>> {
        val response = try {
            api.getLocationList().body()!!.results
        } catch (e : Exception){
            return Resource.Error("Error!")
        }
        return Resource.Success(response)
    }


    suspend fun getCharacters(ids : String) : Resource<CharacterList> {
        val response = try {
            api.getCharacterList(ids)
        } catch (e : Exception) {
            return Resource.Error("Error")
        }
        return Resource.Success(response)
    }

    suspend fun getCharacter(id : String) : Resource<CharacterListItem> {
        val response = try {
            api.getCharacter(id)
        } catch (e : Exception) {
            return Resource.Error("Error")
        }
        return Resource.Success(response)
    }



}