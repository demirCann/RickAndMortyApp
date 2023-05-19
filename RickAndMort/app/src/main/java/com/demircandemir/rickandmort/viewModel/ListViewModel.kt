package com.demircandemir.rickandmort.viewModel

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.demircandemir.rickandmort.model.CharacterListItem
import com.demircandemir.rickandmort.model.Result
import com.demircandemir.rickandmort.repository.RickAndMortRepo
import com.demircandemir.rickandmort.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListViewModel @Inject constructor(
    private val repository : RickAndMortRepo
) : ViewModel() {

    var characterNumberList  = mutableStateOf<List<String>>(listOf())
    var locationList = mutableStateOf<List<Result>>(listOf())
    var errorMessage = mutableStateOf("")
    var characterList = mutableStateOf<List<CharacterListItem>>(listOf())



    init {
        downloadLocation()
    }


    fun downloadLocation(){
        viewModelScope.launch{
            val result = repository.getLocations()
            when(result) {
                is Resource.Success -> {

                    Log.i("MyApp", "viewModelSuccess(location)")
                    val locationItems = result.data!!.mapIndexed { index, item ->
                       Result(item.id, item.name, item.residents, item.url)
                    } as List<Result>

                    errorMessage.value = ""
                    locationList.value += locationItems

                    val characterNumbers = result.data.mapIndexed { index, item ->
                        subString(item.residents)
                    }

                    for (num in characterNumberList.value) {
                        Log.i("MyApp", num)
                        Log.i("MyApp", "naber")
                    }

                    downloadCharacters(characterNumberList.value.toString())

                }
                is Resource.Error -> {
                    Log.i("MyApp", "viewModelError(downloadLocation)")
                    errorMessage.value = result.message!!
                }
                is Resource.Loading -> {
                }
            }
        }
    }

    fun downloadCharacters(ids : String){
        viewModelScope.launch{
            val result = repository.getCharacters(ids)

            when(result){
                is Resource.Success -> {
                    Log.i("MyApp", "viewModelSuccess(character)")
                    val characterItems = result.data!!.mapIndexed { index, item ->
                        CharacterListItem(item.created, item.episode, item.gender, item.id, item.image, item.location, item.name, item.origin, item.species, item.status, item.type, item.url)
                    } as List<CharacterListItem>

                    errorMessage.value = ""
                    characterList.value += characterItems


                }
                is Resource.Error -> {
                    Log.i("MyApp", "viewModelError(downloadCharacters)")
                    errorMessage.value = result.message!!
                }
                is Resource.Loading->{}
            }
        }
    }

    fun subString(strList : List<String>){
        viewModelScope.launch {
            var numList = listOf<String>()

            for (str in strList){
                var substring = str.substringAfterLast("/")
                characterNumberList.value += substring
            }

        }
    }

}