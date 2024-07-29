package com.example.composeconvert

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.offset
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import com.example.composeconvert.DashBoard.rememberChildPadding
import com.example.mytv11.DataCenter

@Composable
fun MovieDetailsScreen(
    goToMoviePlayer: () -> Unit,
    onBackPressed: () -> Unit,
){

    val childPadding = rememberChildPadding()
    BackHandler(onBack = onBackPressed)
    val watchFocusRequester = remember {
        FocusRequester()
    }
    LaunchedEffect(Unit) {
          watchFocusRequester.requestFocus()
    }
   Box(modifier = Modifier.fillMaxSize()){
       Box(modifier = Modifier.fillMaxSize()){
           Image(painter = painterResource(id = DataCenter.getData().image), contentDescription = "", contentScale = ContentScale.FillBounds, modifier = Modifier
               .fillMaxSize()
               .fillMaxSize()
               .drawWithContent {
                   drawContent()
                   drawRect(
                       color = Color.Black.copy(
                           alpha = 0.5f
                       )
                   )
               })
       }
       Box(modifier = Modifier
           .fillMaxSize()
           .offset(20.dp), contentAlignment = Alignment.CenterStart){
                 Column {
                     Text(text = DataCenter.getData().title, fontSize = 30.sp, color = Color.White)
                     Row {
                         Button(onClick = {}, colors = ButtonDefaults.colors(Color.Transparent), modifier = Modifier.focusRequester(watchFocusRequester)) {
                              Text(text = "Watch")
                         }
                         Button(onClick = {}, colors = ButtonDefaults.colors(Color.Transparent)) {
                             Text(text = "Favourites")
                         }
                     }
                 }
       }
   }

}