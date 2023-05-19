package com.demircandemir.rickandmort.viewModel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.demircandemir.rickandmort.model.CharacterListItem
import com.demircandemir.rickandmort.repository.RickAndMortRepo
import com.demircandemir.rickandmort.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(
    private val repository : RickAndMortRepo
) : ViewModel() {

    suspend fun getCharacter(id : String) : Resource<CharacterListItem> {
        return repository.getCharacter(id)
    }
}