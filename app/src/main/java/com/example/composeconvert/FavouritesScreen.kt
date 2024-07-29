package com.example.composeconvert

import android.util.Log
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.tv.foundation.lazy.list.rememberTvLazyListState
import com.example.mytv11.DataCenter
import com.example.mytv11.FilterType
import com.example.mytv11.itemdata

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RecentScreen(
    onMovieClick: (movie: itemdata) -> Unit,
    lazyListStateRecent: LazyListState,
    isComingBackFromDifferentScreen: Boolean,
    lazyRow: FocusRequester,
    lazyColum: FocusRequester
){
    val ( firstItem, immersivelist) = remember { FocusRequester.createRefs() }
    val selectedIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val listState = rememberTvLazyListState()
     val data = DataCenter.getCategories(FilterType.RECENT).map { it.items }

       LaunchedEffect(Unit) {
           Log.e("nish10","$selectedIndex.value")
           if(selectedIndex.value !=-1){
               lazyColum.requestFocus()
               Log.e("nish10","In lancheffect")
           }
       }
     DisposableEffect(Unit) {
         onDispose {
             Log.e("nish10","disposed")
         }
     }



           Log.e("wish5", " in FAV")
            LazyVerticalGrid(columns = GridCells.Fixed(3),
                Modifier
                    .fillMaxSize()
                    .focusGroup()
                    .focusRestorer()
                    .focusRequester(lazyColum)
                    .offset(0.dp, 80.dp)) {


                itemsIndexed(data.flatten()) { index, movie ->
                    MoviesRowItem(
                        movie = movie,
                        onMovieSelected = {onMovieClick(it)
                                           selectedIndex.value = index },
                        index = index
                    )
                }
            }
             
        }

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun favScreen(
    onMovieClick: (movie: itemdata) -> Unit,
    lazyListStateRecent: LazyListState,
    isComingBackFromDifferentScreen: Boolean,
    lazyRow: FocusRequester,
    lazyColum: FocusRequester
){
    val ( firstItem, immersivelist) = remember { FocusRequester.createRefs() }
    val selectedIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val listState = rememberTvLazyListState()
    val data = DataCenter.getCategories(FilterType.FAVORITE).map { it.items }

    LaunchedEffect(Unit) {
        if(selectedIndex.value !=-1){
            lazyColum.requestFocus()
        }
    }



    Log.e("wish5", " in FAV")
    LazyVerticalGrid(columns = GridCells.Fixed(3),
        Modifier
            .fillMaxSize()
            .focusGroup()
            .focusRestorer()
            .focusRequester(lazyColum)
            .offset(0.dp, 80.dp)) {


        itemsIndexed(data.flatten()) { index, movie ->
            MoviesRowItem(
                movie = movie,
                onMovieSelected = {onMovieClick(it)
                    selectedIndex.value = index },
                index = index
            )
        }
    }

}



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SearchScreen(
    onMovieClick: (movie: itemdata) -> Unit,
    lazyListStateRecent: LazyListState,
    isComingBackFromDifferentScreen: Boolean,
    lazyRow: FocusRequester,
    lazyColum: FocusRequester
){
    val ( firstItem, immersivelist) = remember { FocusRequester.createRefs() }
    val selectedIndex = rememberSaveable {
        mutableStateOf(-1)
    }
    val listState = rememberTvLazyListState()
    val searchitems = rememberSaveable {
        mutableStateOf("")
    }
    val data = remember {
        mutableStateOf<List<List<itemdata>>?>(null)
    }
    data.value = DataCenter.searchCategoriesByTitleInItems(searchitems.value).map { it.items }
    LaunchedEffect(Unit) {
       firstItem.requestFocus()
        Log.e("nish13","$selectedIndex")
        if(selectedIndex.value !=-1){
            lazyColum.requestFocus()
            Log.e("nish13","done")
        }
    }



  Column {

      TextField(value = searchitems.value, onValueChange = {searchitems.value = it}, modifier = Modifier
          .fillMaxWidth()
          .offset(0.dp, 50.dp)
          .focusRequester(firstItem),
          colors= TextFieldDefaults.textFieldColors(Color.White))
      Log.e("wish5", " in FAV")
      LazyVerticalGrid(columns = GridCells.Fixed(3),
          Modifier
              .fillMaxSize()
              .focusGroup()
              .focusRestorer()
              .focusRequester(lazyColum)
              .offset(0.dp, 40.dp)) {


          itemsIndexed(data.value!!.flatten()) { index, movie ->
              MoviesRowItem(
                  movie = movie,
                  onMovieSelected = {onMovieClick(it)
                      selectedIndex.value = index },
                  index = index
              )
          }

  }


    }

}






