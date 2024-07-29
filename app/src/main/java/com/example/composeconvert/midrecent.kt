package com.example.composeconvert

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import com.example.mytv11.itemdata

@Composable
fun minrecent(onMovieClick: (movie: itemdata) -> Unit,
              lazyListStateRecent: LazyListState,
              isComingBackFromDifferentScreen: Boolean,
              lazyRow: FocusRequester,
              lazyColum: FocusRequester
) {
    RecentScreen(
        onMovieClick = onMovieClick,
        lazyListStateRecent =  lazyListStateRecent,
        lazyRow = lazyRow,
        lazyColum=lazyColum,
        isComingBackFromDifferentScreen =   isComingBackFromDifferentScreen
    )

}