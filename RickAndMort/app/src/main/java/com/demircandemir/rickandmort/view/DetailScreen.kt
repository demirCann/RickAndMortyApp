package com.demircandemir.rickandmort.view

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.demircandemir.rickandmort.model.CharacterListItem
import com.demircandemir.rickandmort.util.Resource
import com.demircandemir.rickandmort.viewModel.DetailViewModel
import kotlinx.coroutines.launch


@Composable
fun DetailScreen(id : String, characterName : String ,navController: NavController, onBackClick : () -> Unit , viewModel : DetailViewModel = hiltViewModel())  {


    val characterItem = produceState<Resource<CharacterListItem>>(initialValue = Resource.Loading()) {
        value = viewModel.getCharacter(id)
    }.value
    
    Scaffold (
        topBar = {
            TopAppBar(backgroundColor = MaterialTheme.colors.background,
                title = { Text(text = characterName,
                textAlign = TextAlign.Center,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.h4) },

                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.Filled.ArrowBack,
                        contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->

    LazyColumn {
        item {
            Box(modifier = Modifier.background(MaterialTheme.colors.background)){
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    when(characterItem) {
                        is Resource.Success -> {
                            val selectedCharacter = characterItem.data!!

                            Image(painter = rememberImagePainter(data = selectedCharacter.image),
                                contentDescription = selectedCharacter.name,
                                modifier = Modifier
                                    .padding(horizontal = 50.dp, vertical = 20.dp)
                                    .size(275.dp, 275.dp)
                            )
                            Row() {

                                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 20.dp)) {

                                    Text(text = "Status",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Specy",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Gender",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Origin",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Location",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Episodes",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = "Created at (in API)",
                                        fontSize = 22.sp,
                                        fontWeight = FontWeight.Bold)
                                }
                                Column(modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp)) {
                                    val episodeList = subString(selectedCharacter.episode)

                                    Text(text = selectedCharacter.status,
                                        fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = selectedCharacter.species,
                                        fontSize = 22.sp,
                                    )
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = selectedCharacter.gender,
                                        fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = selectedCharacter.origin.name,
                                        fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = selectedCharacter.location.name,
                                        fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = episodeList,
                                        fontSize = 22.sp)
                                    Spacer(modifier = Modifier.height(5.dp))
                                    Text(text = selectedCharacter.created,
                                        fontSize = 22.sp)
                                }
                            }
                        }
                        is Resource.Error -> {
                            Text(characterItem.message!!)
                        }
                        is Resource.Loading -> {}
                    }
                }
            }
        }
     }
    }
}



fun subString(strList : List<String>): String {

        var numList = listOf<String>()
        for (str in strList){
            var substring = str.substringAfterLast("/")
            numList += substring
        }

    return numList.toString()
}

