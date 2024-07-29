package com.example.composeconvert.DashBoard

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onPreviewKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.composeconvert.All
import com.example.composeconvert.Padding
import com.example.composeconvert.Screens
import com.example.composeconvert.SearchScreen
import com.example.composeconvert.favScreen
import com.example.composeconvert.minrecent
import com.example.mytv11.itemdata


val ParentPadding = PaddingValues(vertical = 16.dp, horizontal = 58.dp)
@Composable
fun rememberChildPadding(direction: LayoutDirection = LocalLayoutDirection.current): Padding {
    return remember {
        Padding(
            start = ParentPadding.calculateStartPadding(direction) + 8.dp,
            top = ParentPadding.calculateTopPadding(),
            end = ParentPadding.calculateEndPadding(direction) + 8.dp,
            bottom = ParentPadding.calculateBottomPadding()
        )
    }
}
@Composable
fun DashboardScreen(
    openCategoryMovieList: (categoryId: String) -> Unit,
    openMovieDetailsScreen: (movieId: itemdata) -> Unit,
    openVideoPlayer: () -> Unit,
    isComingBackFromDifferentScreen: Boolean,
    resetIsComingBackFromDifferentScreen: () -> Unit,
    onBackPressed: () -> Unit
){
    val see = rememberSaveable {
        mutableStateOf(-1)
    }
    val carousefocusrequester = remember {
        FocusRequester()
    }
    val lazyRow =  remember {
        FocusRequester()
    }
   val lazyColum = remember {
        FocusRequester()
    }
    val lazyListState = rememberLazyListState()
    val lazyListStateRecent = rememberLazyListState()

    val navControllerBanner = rememberNavController()

    val focusManager = LocalFocusManager.current
    val navController = rememberNavController()
    var isTopBarFocused by remember { mutableStateOf(false) }
    var iscomingbackformmovielist by remember {
        mutableStateOf(false)
    }
    var currentDestination: String? by remember { mutableStateOf(null) }
    val currentTopBarSelectedTabIndex by remember(currentDestination) {
        derivedStateOf {
            currentDestination?.let { TopBarTabs.indexOf(Screens.valueOf(it)) } ?: 0
        }
    }
    DisposableEffect(Unit) {
        val listener = NavController.OnDestinationChangedListener { _, destination, _ ->
            currentDestination = destination.route
            Log.e("nish10","$currentDestination")
        }

        navController.addOnDestinationChangedListener(listener)

        onDispose {
            Log.e("nish3","Dicomposed")
            navController.removeOnDestinationChangedListener(listener)
        }
    }

    BackPressHandledArea(
       onBackPressed = {   if(isTopBarFocused == true){  if (currentTopBarSelectedTabIndex == 0) {onBackPressed()} else {
           TopBarFocusRequesters[0].requestFocus()}} else { TopBarFocusRequesters[currentTopBarSelectedTabIndex].requestFocus()} },
        modifier = Modifier.background(brush = Brush.verticalGradient(colors = listOf(Color.Transparent, Color.Black.copy(alpha = 1f))))

    )
    {

        var wasTopBarFocusRequestedBefore by rememberSaveable { mutableStateOf(false) }
        LaunchedEffect(Unit) {
            Log.e("nish14","$see")
            if (!wasTopBarFocusRequestedBefore) {
                TopBarFocusRequesters[currentTopBarSelectedTabIndex].requestFocus()
                wasTopBarFocusRequestedBefore = true
            }
            else{
                Log.e("nish10","$currentTopBarSelectedTabIndex")
                TopBarFocusRequesters[currentTopBarSelectedTabIndex].requestFocus()

            }
        }
        Box(modifier = Modifier
            .fillMaxSize()
            .focusable(true)){


            //body
            Body(
                openCategoryMovieList = openCategoryMovieList,
                openMovieDetailsScreen = openMovieDetailsScreen,
                openVideoPlayer = openVideoPlayer,
                navController = navController,
                modifier = Modifier,
                carousefocusrequester = carousefocusrequester,
                lazyListState = lazyListState,
                isComingBackFromDifferentScreen = isComingBackFromDifferentScreen,
                navControllerBanner = navControllerBanner,
                iscomingbackformmovielist = iscomingbackformmovielist,
                changed =  {iscomingbackformmovielist = it},
                resetIsComingBackFromDifferentScreen = {resetIsComingBackFromDifferentScreen()},
                lazyListStateRecent = lazyListStateRecent,
                lazyRow = lazyRow,
                lazyColum = lazyColum

                )
        }
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopStart){
            //DashTopbard
            DashboardTopBar(
                lazyColum =lazyColum,
                lazyRow = lazyRow,
                changed = {iscomingbackformmovielist = it},
                navControllerBanner =navControllerBanner,
                iscomingbackformmovielist =iscomingbackformmovielist,
                isComingBackFromDifferentScreen =isComingBackFromDifferentScreen ,
                resetIsComingBackFromDifferentScreen = {resetIsComingBackFromDifferentScreen()},
                carousefocusrequester =carousefocusrequester,
                lazyListState = lazyListState,
                modifier = Modifier

                    .onFocusChanged {
                        isTopBarFocused = it.hasFocus

                    }
                    .padding(
                        horizontal = ParentPadding.calculateStartPadding(
                            LocalLayoutDirection.current
                        ) + 8.dp
                    )
                    .padding(
                        top = ParentPadding.calculateTopPadding(),
                        bottom = ParentPadding.calculateBottomPadding()
                    ),
                selectedTabIndex = currentTopBarSelectedTabIndex,
            ) { screen ->
                navController.navigate(screen()) {
                    if (screen == TopBarTabs[0]) popUpTo(TopBarTabs[0].invoke())
                    launchSingleTop = true
                }
            }

        }
    }

}
@Composable
private fun BackPressHandledArea(
    onBackPressed: () -> Unit,
    modifier: Modifier = Modifier,
    content: @Composable BoxScope.() -> Unit,
) =
    Box(
        modifier = Modifier
            .onPreviewKeyEvent {
                if (it.key == Key.Back && it.type == KeyEventType.KeyUp) {
                    onBackPressed()
                    true
                } else {
                    false
                }
            }
            .then(modifier),
        content = content
    )


@Composable
private fun Body(
    openCategoryMovieList: (categoryId: String) -> Unit,
    openMovieDetailsScreen: (movieId: itemdata) -> Unit,
    openVideoPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController(),
    carousefocusrequester: FocusRequester,
    lazyListState: LazyListState,
    isComingBackFromDifferentScreen: Boolean,
    navControllerBanner: NavHostController,
    iscomingbackformmovielist: Boolean,
    changed: (ischanged: Boolean) -> Unit,
    resetIsComingBackFromDifferentScreen: () -> Unit,
    lazyListStateRecent: LazyListState,
    lazyRow: FocusRequester,
    lazyColum: FocusRequester

) {
    val focusManager = LocalFocusManager.current


    NavHost(navController = navController, startDestination = Screens.All()) {
        composable(route = Screens.All()){
           Log.e("nish", "in All")
            All(
                OnUp = { TopBarFocusRequesters[0].requestFocus()},
                carousefocusrequester = carousefocusrequester,
                onMovieClick = {
                    openMovieDetailsScreen(it)
                },
                goToVideoPlayer = openVideoPlayer,
                lazyListState = lazyListState,
                isComingBackFromDifferentScreen = isComingBackFromDifferentScreen,
                navControllerBanner =navControllerBanner,
                iscomingbackformmovielist =iscomingbackformmovielist,
                change = {changed(true)},
                resetIsComingBackFromDifferentScreen = {resetIsComingBackFromDifferentScreen()},
                lazyColum =lazyColum,

                )
        }
        composable(route = Screens.Recent()){
            Log.e("nish", "in Recent ")
            minrecent(
                onMovieClick = openMovieDetailsScreen,
                lazyListStateRecent =  lazyListStateRecent,
                lazyRow = lazyRow,
                lazyColum=lazyColum,
                isComingBackFromDifferentScreen =   isComingBackFromDifferentScreen
            )

        }
        composable(route = Screens.Favourites()){

            Log.e("nish", "in Fav")
            favScreen(
                onMovieClick = openMovieDetailsScreen,
                lazyListStateRecent =  lazyListStateRecent,
                lazyRow = lazyRow,
                lazyColum=lazyColum,
                isComingBackFromDifferentScreen =   isComingBackFromDifferentScreen
            )

        }
        composable(route = Screens.Categories()){
            Text(text = "In categories")
            Log.e("nish", "in cat")

        }
        composable(route = Screens.Search()){
            Log.e("nish", "in search")
            SearchScreen(
                onMovieClick = openMovieDetailsScreen,
                lazyListStateRecent =  lazyListStateRecent,
                lazyRow = lazyRow,
                lazyColum=lazyColum,
                isComingBackFromDifferentScreen =   isComingBackFromDifferentScreen
            )

        }
    }
}