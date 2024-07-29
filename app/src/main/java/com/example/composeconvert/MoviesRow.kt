package com.example.composeconvert

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Border
import androidx.tv.material3.ClickableSurfaceDefaults
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.StandardCardContainer
import androidx.tv.material3.Surface
import com.example.composeconvert.DashBoard.rememberChildPadding
import com.example.mytv11.DataCenter
import com.example.mytv11.itemdata

enum class ItemDirection(val aspectRatio: Float) {
    Vertical(2.5f / 3.5f),  // Slightly reduced vertical aspect ratio
    Horizontal(3.5f / 2.5f); // Slightly reduced horizontal aspect ratio
}
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoviesRow(
    movieList: Category,
    modifier: Modifier,
    itemDirection: ItemDirection ,
    startPadding: Dp = rememberChildPadding().start,
    endPadding: Dp = rememberChildPadding().end,
    title: String? = null,
    lazyRow: FocusRequester,
    firstItem: FocusRequester,
    showItemTitle: Boolean = true,
    showIndexOverImage: Boolean = false,
    onMovieSelected: (movie: itemdata) -> Unit = {},
    onMovieFocused: (movie: itemdata) -> Unit = {},
    isComingBackFromDifferentScreen: Boolean,
    selectedIndex: Int,
    index1: Int,
){
    val coroutineScope = rememberCoroutineScope()
Log.e("nish6","SelectItem is at $selectedIndex")
LaunchedEffect(Unit) {
    Log.e("nish12","$selectedIndex")
   if(selectedIndex==index1){
       lazyRow.requestFocus()
       Log.e("nish5"," In MOVIE ROW${index1}")
   }
}
DisposableEffect(Unit) {
    onDispose {
        Log.e("nish","movie row destroyed")
    }
}

    Column(
        modifier = modifier
            .focusGroup()
            .fillMaxSize()
    ){
        Log.e("nish6","Movie Row $index1")

        Text(text = movieList.title , fontSize = 30.sp, color = MaterialTheme.colorScheme.onSurface ,  modifier = Modifier
            .alpha(1f)
            .padding(start = startPadding, top = 16.dp, bottom = 20.dp))
        LazyRow ( contentPadding = PaddingValues(
            start = startPadding,
            end = endPadding,
        ),
            horizontalArrangement = Arrangement.spacedBy(20.dp),
            modifier = Modifier
                .focusGroup()

                .focusRequester(lazyRow)
                .focusRestorer {
                    firstItem
                }){
            itemsIndexed(movieList.items){ index, movie ->
                val itemModifier = if (index == 0) {
                    Modifier.focusRequester(firstItem)
                } else {
                    Modifier
                }
                MoviesRowItem(
                    modifier = itemModifier.weight(1f),
                    index = index,
                    itemDirection = itemDirection,
                    onMovieSelected = {
                        lazyRow.saveFocusedChild()

                        onMovieSelected(it)
                    },
                    onMovieFocused = onMovieFocused,
                    movie = movie,
                )
            }
           }
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MoviesRowItem(
    index: Int,
    movie: itemdata,
    onMovieSelected: (Movie:itemdata) -> Unit,

    modifier: Modifier = Modifier,
    itemDirection: ItemDirection = ItemDirection.Horizontal,
    onMovieFocused: (Movie:itemdata) -> Unit = {},
){
    var isFocused by remember { mutableStateOf(false) }

    MovieCard(
        onClick = {  DataCenter.setData(movie)
            onMovieSelected(movie) },
        title = {
            MoviesRowItemText(
                isItemFocused = isFocused,
                movie = movie
            )
        },
        modifier = Modifier
            .onFocusChanged {
                isFocused = it.isFocused
                Log.e("nish", "$isFocused")
                if (it.isFocused) {
                    DataCenter.setData(movie)
                    onMovieFocused(movie)

                }
                Log.e("nish4", "is movieItem focused $isFocused")
            }
            .focusProperties {
                left = if (index == 0) {
                    FocusRequester.Cancel
                } else {
                    FocusRequester.Default
                }
            }
            .then(modifier)
    ) {

        MoviesRowItemImage(
            isItemFocused = isFocused,

            modifier = Modifier
                .aspectRatio(itemDirection.aspectRatio)
                .size(200.dp),
            movie = movie,
            index = index
        )
    }
}

@Composable
fun MovieCard(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    title: @Composable () -> Unit = {},
    image: @Composable BoxScope.() -> Unit,
) {
    StandardCardContainer(
        modifier = modifier,
        title = title,
        imageCard = {
            Surface(
                onClick = onClick,
                shape = ClickableSurfaceDefaults.shape(JetStreamCardShape),
                border = ClickableSurfaceDefaults.border(
                    focusedBorder = Border(
                        border = BorderStroke(
                            width = 3.dp,
                            color = MaterialTheme.colorScheme.onSurface
                        ),
                    shape = JetStreamCardShape
                    )
                ),
                scale = ClickableSurfaceDefaults.scale(focusedScale = 1f),
                content = image
            )
        },
    )
}
@Composable
private fun MoviesRowItemText(

    isItemFocused: Boolean,
    movie: itemdata,
    modifier: Modifier = Modifier
) {

        val movieNameAlpha by animateFloatAsState(
            targetValue = if (isItemFocused) 1f else 0f,
            label = "",
        )
        Text(
            text = movie.title,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyMedium.copy(
                fontWeight = FontWeight.SemiBold
            ), fontSize = 20.sp,
            textAlign = TextAlign.Center,
            modifier = modifier
                .alpha(movieNameAlpha)
                .fillMaxWidth()
                .padding(top = 4.dp),
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
@Composable
private fun MoviesRowItemImage(

    movie: itemdata,
    index: Int,
    modifier: Modifier = Modifier,
    isItemFocused: Boolean,
) {
    Box(contentAlignment = Alignment.CenterStart) {
        Image(
            painter = painterResource(id = movie.image),
            contentScale = ContentScale.Crop,
            contentDescription = "",
            modifier = modifier

                .fillMaxWidth()
                .drawWithContent {
                    drawContent()
                    if (isItemFocused) {
                        drawRect(
                            color = Color.Black.copy(
                                alpha = 0f
                            )
                        )
                    } else {
                        drawRect(
                            color = Color.Black.copy(
                                alpha = 0.5f
                            )
                        )
                    }


                },
        )
    }
}