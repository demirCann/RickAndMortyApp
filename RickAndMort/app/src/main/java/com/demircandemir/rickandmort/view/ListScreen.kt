package com.demircandemir.rickandmort.view

import android.annotation.SuppressLint
import android.util.Log
import android.view.MenuItem
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.*

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.SnackbarDefaults.backgroundColor
import com.demircandemir.rickandmort.model.Result
import androidx.compose.runtime.Composable
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.modifier.modifierLocalConsumer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.annotation.ExperimentalCoilApi
import coil.compose.rememberImagePainter
import com.demircandemir.rickandmort.model.CharacterListItem
import com.demircandemir.rickandmort.model.Location
import com.demircandemir.rickandmort.repository.RickAndMortRepo
import com.demircandemir.rickandmort.ui.theme.RickAndMortTheme
import com.demircandemir.rickandmort.viewModel.ListViewModel
import kotlinx.coroutines.launch
import kotlin.collections.indexOf as indexOf1


@Composable
fun ListScreen (navController: NavController,onCharacterItemClick : (Int, String) -> Unit, viewModel: ListViewModel = hiltViewModel()) {

    val locationList by remember { viewModel.locationList }
    val characterList by remember { viewModel.characterList }



    val lazyListState = rememberLazyListState()

    val coroutineScope = rememberCoroutineScope()




        Box(modifier = Modifier.background(MaterialTheme.colors.background)) {
            Column(modifier = Modifier.padding()) {

                if (locationList.isNotEmpty() and characterList.isNotEmpty()){
                    LocationTabs(locations = locationList, selectedLocation = lazyListState.firstVisibleItemIndex.getLocation(locationList, characterList), onLocationSelected = { location ->
                        coroutineScope.launch { lazyListState.scrollToItem(location.getIndex(locationList, characterList)) }
                    } )
                }

                LazyColumn(state = lazyListState) {
                    for (location in locationList) {
                        val characterItems = characterList.filter { it.location.name == location.name }
                       itemsIndexed(characterItems) { index, characterItem ->
                            CharacterItem(characterListItem = characterItem, onClick = { onCharacterItemClick(characterItem.id, characterItem.name) })
                           if(index != characterItems.lastIndex){}
                               //Divider(modifier = Modifier.padding(horizontal = 16.dp))
                       }
                    }
                    item {
                        Spacer(modifier = Modifier.height(80.dp))
                    }
                }
            }
        }
}

private fun Int.getLocation (locations: List<Result>, characters: List<CharacterListItem>) : Result {
    return locations.last { it.getIndex(locations, characters) <= this }
}


private fun Result.getIndex (locations: List<Result>, characters: List<CharacterListItem>) : Int {
    var index = 0
    for (i in 0 until locations.indexOf(this)){
        index += 1
        index += characters.filter { it.location.name == locations[i].name }.size
    }
    return index
}



@Composable
fun LocationTabs(
    locations : List<Result>,
    selectedLocation : Result,
    onLocationSelected : (Result) -> Unit
){
    ScrollableTabRow(selectedTabIndex = locations.indexOf(selectedLocation),
    backgroundColor = MaterialTheme.colors.background,
    contentColor = MaterialTheme.colors.onSurface,
    edgePadding = 8.dp,
    indicator = {},
        divider = {}
    ) {
        locations.forEach { location ->
            LocationTab(location = location, selected = location == selectedLocation, onClick = { onLocationSelected(location) }, modifier = Modifier.padding(horizontal = 8.dp, vertical = 16.dp))
        }
    }
}

private enum class LocationTabState {Selected, NotSelected}

@Composable
private fun LocationTab(
    location: Result,
    selected: Boolean,
    onClick : () -> Unit,
    modifier: Modifier = Modifier
) {


    val transition = updateTransition(if (selected) LocationTabState.Selected else LocationTabState.NotSelected,
        label = ""
    )

    val backroundColor by transition.animateColor(label = "") { state ->
        when(state) {
            LocationTabState.Selected -> MaterialTheme.colors.primary
            LocationTabState.NotSelected -> MaterialTheme.colors.background
        }
    }

    val contentColor by transition.animateColor(label = "") { state ->
        when(state){
            LocationTabState.Selected -> LocalContentColor.current
            LocationTabState.NotSelected -> MaterialTheme.colors.primary
        }
    }

    Surface(
        modifier = modifier,
        shape = MaterialTheme.shapes.small,
        color = backgroundColor,
        contentColor = contentColor,
        border = BorderStroke(2.dp, Color.Black)
    ) {
        Row(
            modifier = Modifier
                .clickable(onClick = onClick)
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {

            Text(
                text = location.name,
                style = MaterialTheme.typography.button
            )
        }
    }


}


@Composable
fun CharacterItem(
    characterListItem: CharacterListItem,
    onClick: () -> Unit,
) {

    if(characterListItem.gender == "Female") {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colors.secondary,
            contentColor = MaterialTheme.colors.onSurface
        ) {
            Row(modifier = Modifier
                .clickable(onClick = onClick)
                .border(width = 2.dp, color = Color.Black)
                .padding(16.dp)
            ) {
                Box(modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.background),
                ) {
                    Image(
                        painter = rememberImagePainter(data = characterListItem.image),
                        contentDescription = characterListItem.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.0f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = characterListItem.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(5.dp)
                        .background(Color.White),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }

        }
    }

    if(characterListItem.gender == "Male") {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colors.primary,
            contentColor = MaterialTheme.colors.onSurface
        ) {
            Row(modifier = Modifier
                .clickable(onClick = onClick)
                .border(width = 2.dp, color = Color.Black)
                .padding(16.dp)
            ) {
                Box(modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.background),
                ) {
                    Image(
                        painter = rememberImagePainter(data = characterListItem.image),
                        contentDescription = characterListItem.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.0f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = characterListItem.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(5.dp)
                        .background(Color.White),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }

        }
    }

    if(characterListItem.gender == "unknown") {
        Surface(
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp),
            color = MaterialTheme.colors.primaryVariant,
            contentColor = MaterialTheme.colors.onSurface
        ) {
            Row(modifier = Modifier
                .clickable(onClick = onClick)
                .border(width = 2.dp, color = Color.Black)
                .padding(16.dp)
            ) {
                Box(modifier = Modifier
                    .size(120.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colors.background),
                ) {
                    Image(
                        painter = rememberImagePainter(data = characterListItem.image),
                        contentDescription = characterListItem.name,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.0f)
                    )
                }

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = characterListItem.name,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.h5,
                    modifier = Modifier
                        .padding(5.dp)
                        .background(Color.White),
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

            }

        }
    }
}



/*@Composable
fun LocationList(navController: NavController, viewModel : ListViewModel = hiltViewModel()
){
    val locationList by remember {viewModel.locationList}
    val errorMessage by remember {viewModel.errorMessage}


    LocationListView(navController = navController, locations = locationList)

    /*if (locationList.isEmpty()){
        Log.i("MyApp", "burda")
    }*/
    
    Box(contentAlignment = Alignment.Center,
        ) {
        if (errorMessage.isNotEmpty()) {

            Text(
                text = "Error!!!",
                fontSize = 100.sp
            )
        }
    }
}


@Composable
fun LocationListView(navController: NavController, locations : List<Result>) {
    LazyRow(Modifier.padding(start = 10.dp, top = 5.dp, end = 10.dp),) {
        items(locations) { location ->
            LocationColumn(navController = navController, location = location)
        }
    }
}




@Composable
fun LocationColumn (navController: NavController, location : Result) {
    
    Row(modifier = Modifier
        .size(width = 160.dp, height = 50.dp)
        .background(color = MaterialTheme.colors.secondary)
        /*.clickable {
            navController.navigate(

            )
        }*/

    ) {

       // Log.i("MyApp", location.name)

        Text(text = location.name,
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.h6,
            modifier = Modifier
                .padding(4.dp)
                .border(width = 4.dp, color = Color.Black)
                .size(140.dp, 60.dp)
                .background(Color.White),
            fontWeight = FontWeight.Bold,
            color = Color.Black
            )
    }
}


@Composable
fun CharacterList(navController: NavController, viewModel: ListViewModel = hiltViewModel()){
    val characterList by remember { viewModel.characterList }
    val errorMessage by remember { viewModel.errorMessage }



    CharacterListView(navController = navController, characters = characterList)


        if (errorMessage.isNotEmpty()) {
            Log.i("MyApp", "Error!!!")

        }
}




@Composable
fun CharacterListView(navController: NavController, characters : List<CharacterListItem>){
    LazyColumn(Modifier.padding(start = 10.dp, top = 50.dp, end = 10.dp, bottom = 10.dp)) {
        items(characters) {character ->
            CharacterRow(navController = navController, character = character)
        }
    }
}




@OptIn(ExperimentalCoilApi::class)
@Composable
fun CharacterRow (navController: NavController, character : CharacterListItem) {



    Box() {


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .background(color = MaterialTheme.colors.secondary)
                .clickable {
                    navController.navigate(
                        "character_detail_screen/${character.id}"
                    )
                }
        ) {
            //Log.i("MyApp", "Çalışyor ya olum")
            Image(
                painter = rememberImagePainter(data = character.image),
                contentDescription = character.name,
                modifier = Modifier
                    .size(100.dp, 100.dp)
                    .border(2.dp, Color.Black, CircleShape)
            )

            Spacer(modifier = Modifier.padding(10.dp))

            Text(
                text = character.name,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.h6,
                modifier = Modifier
                    .padding(5.dp)
                    .border(width = 4.dp, color = Color.Black)
                    .background(Color.White),
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}*/


