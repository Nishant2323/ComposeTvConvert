package com.example.composeconvert

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.tv.material3.MaterialTheme
import com.example.mytv11.DataCenter
import com.example.mytv11.itemdata
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun All(
    OnUp: () -> Unit,
    carousefocusrequester: FocusRequester,
    modifier: Modifier = Modifier,
    onMovieClick: (movie: itemdata) -> Unit,
    goToVideoPlayer: () -> Unit,
    lazyListState: LazyListState,
    isComingBackFromDifferentScreen: Boolean,
    change: (ischanged: Boolean) -> Unit,
    navControllerBanner: NavHostController,
    iscomingbackformmovielist: Boolean,
    resetIsComingBackFromDifferentScreen: () -> Unit,
    lazyColum: FocusRequester,) {
    val (lazyRow, lazyColumn ,firstItem, immersivelist) = remember { FocusRequester.createRefs() }

    val selectedIndex = rememberSaveable{
        mutableIntStateOf(-1)
    }


   val isimmersiveFocused  = remember {
       mutableStateOf(false)
   }
    val focusManager = LocalFocusManager.current
    val coroutineScope = rememberCoroutineScope()
    if (isComingBackFromDifferentScreen) {
        LaunchedEffect(isComingBackFromDifferentScreen) {
            resetIsComingBackFromDifferentScreen()
        }
    }
    Column(
      ) {


              NavHost(navController = navControllerBanner, startDestination = Banner.Carusole()) {
                  composable(route = Banner.Carusole()){
                      FeaturedMoviesCarousel(
                          OnUp,
                          carousefocusrequester = carousefocusrequester,
                          goToVideoPlayer = goToVideoPlayer,
                          modifier = Modifier
                              .fillMaxWidth()
                              .height(324.dp)
                          /*
                           Setting height for the FeaturedMovieCarousel to keep it rendered with same height,
                           regardless of the top bar's visibility
                           */
                      )
                  }
                  composable(route = Banner.ImersiveList()){

                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(324.dp)
                                .focusRequester(immersivelist)
                                .onFocusChanged {
                                    isimmersiveFocused.value = it.isFocused
                                    Log.e(
                                        "nish2",
                                        "in immersivelistfocused is ${isimmersiveFocused.value}"
                                    )
                                }

                        )
                        {
                            Box(modifier = Modifier.fillMaxSize()){
                                //image
                                Image(painter = painterResource(id = DataCenter.getData().image) , contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier
                                    .fillMaxSize()
                                    .drawWithContent {
                                        drawContent()
                                        drawRect(
                                            brush = Brush.linearGradient(
                                                colors = listOf(

                                                    Color.Black, Color.Transparent
                                                )
                                            )
                                        )
                                    })
                            }
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomStart){
                                // text
                                Text(text = DataCenter.getData().title, fontSize = 30.sp, color = MaterialTheme.colorScheme.onSurface , modifier = Modifier.offset(30.dp,-30.dp))
                            }
                        }
                  }
              }
        LazyColumn( state = lazyListState) {
            Log.e("nish12","${selectedIndex}")
            itemsIndexed(DataCenter.allCategories){ index, movie->
                val itemModifier  =  if(index == 0){
                    Modifier
                        .focusRequester(lazyColum)
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.DirectionUp && keyEvent.type == KeyEventType.KeyDown) {
                                Log.e("nish1", "in Index0")
                                navControllerBanner.navigate(Banner.Carusole())
                                selectedIndex.value = -1
//                                focusManager.moveFocus( FocusDirection.Up)
                                var job = coroutineScope.launch {
                                    delay(500)
                                    carousefocusrequester.requestFocus()

                                }

                                true // Indicate that the event was handled
                            }
                            else if (keyEvent.key == Key.Back && keyEvent.type == KeyEventType.KeyDown) {
                                Log.e("nish1", "in Index0")
                                selectedIndex.value = -1

                               false // Indicate that the event was handled
                            }

                            else {
                                false // Allow other handlers to process this event
                            }


                        }
                        .padding(top = 2.dp)

                }
                else{
                    Modifier.padding(top = 2.dp)
                }
                Log.e("nish5","in Lazy colom")
                MoviesRow(
                    movieList = movie,
                    modifier =itemModifier,
                    itemDirection = ItemDirection.Horizontal,
                    lazyRow = lazyRow,
                    firstItem =firstItem,
                    onMovieSelected = {
                        onMovieClick(it)
                        selectedIndex.value=index
                    },
                    onMovieFocused = { navControllerBanner.navigate(Banner.ImersiveList())
                               change(true)    },
                    isComingBackFromDifferentScreen =isComingBackFromDifferentScreen,
                    selectedIndex =selectedIndex.value,
                    index1 = index,
                )
            }
        }


      }
}
