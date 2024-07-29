package com.example.composeconvert

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.PlayArrow
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shadow
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.tv.material3.Button
import androidx.tv.material3.ButtonDefaults
import androidx.tv.material3.Carousel
import androidx.tv.material3.CarouselDefaults
import androidx.tv.material3.CarouselState
import androidx.tv.material3.ExperimentalTvMaterial3Api
import androidx.tv.material3.Icon
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.ShapeDefaults
import androidx.tv.material3.Text

@OptIn(ExperimentalTvMaterial3Api::class)
val CarouselSaver = Saver<CarouselState, Int>(
    save = { it.activeItemIndex },
    restore = { CarouselState(it) }
)


@OptIn(
    ExperimentalTvMaterial3Api::class
)
@Composable
fun FeaturedMoviesCarousel(
    OnUp :() -> Unit,
    goToVideoPlayer: () -> Unit,
    modifier: Modifier = Modifier,
    carousefocusrequester: FocusRequester
) {
    val carouselItems = listOf(
        CarouselItem("", "", painterResource(id = R.drawable.drstone)),
        CarouselItem("", "", painterResource(id = R.drawable.demonslayer)),
        CarouselItem("", "", painterResource(id = R.drawable.kaiju))
    )
    val carouselState = rememberSaveable(saver = CarouselSaver) { CarouselState(0) }
    var isCarouselFocused by remember { mutableStateOf(false) }
    val alpha = if (isCarouselFocused) {
        1f
    } else {
        0f
    }
 Carousel(

     modifier = modifier.border(
         width = JetStreamBorderWidth,
         color = MaterialTheme.colorScheme.onSurface.copy(alpha = alpha),
         shape = ShapeDefaults.Medium,
     )
         .clip(ShapeDefaults.Medium)
         .focusGroup()
         .onKeyEvent { keyEvent ->
             if (keyEvent.key == Key.DirectionUp && keyEvent.type == KeyEventType.KeyUp) {
                OnUp()
                 true // Indicate that the event was handled
             } else {
                 false // Allow other handlers to process this event
             }
         }
         .focusRequester(carousefocusrequester)
         .onFocusChanged {
             // Because the carousel itself never gets the focus
             isCarouselFocused = it.hasFocus
         }

         .onKeyEvent { keyEvent ->
             if (keyEvent.key == Key.Enter && keyEvent.type == KeyEventType.KeyUp) {
                 goToVideoPlayer()
                 true // Indicate that the event was handled
             } else {
                 false // Allow other handlers to process this event
             }
         },

             itemCount = carouselItems.size,
        carouselState = carouselState,
     carouselIndicator = {
         Box(
             modifier = Modifier
                 .fillMaxWidth()
                 .align(Alignment.BottomCenter)
                 .padding(16.dp)
         ) {
             CarouselDefaults.IndicatorRow(
                 itemCount = carouselItems.size,
                 activeItemIndex = carouselState.activeItemIndex,
                 modifier = Modifier.align(Alignment.Center)
             )
         }
     }, contentTransformStartToEnd = fadeIn(tween(durationMillis = 1000))
         .togetherWith(fadeOut(tween(durationMillis = 1000))),
     contentTransformEndToStart = fadeIn(tween(durationMillis = 1000))
         .togetherWith(fadeOut(tween(durationMillis = 1000))),
     content = {index ->
    val movie = carouselItems[index]
    // background
    CarouselItemBackground(carouselItem = movie, modifier = Modifier.fillMaxSize())
    // foreground
    CarouselItemForeground(
        carouselItem = movie,
        isCarouselFocused = isCarouselFocused,
        modifier = Modifier.fillMaxSize()
    )
}  )



}


data class CarouselItem(
    val title: String,
    val description: String,
    val imageUrl: Painter
)



@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun CarouselItemForeground(
    carouselItem: CarouselItem,
    modifier: Modifier = Modifier,
    isCarouselFocused: Boolean = false
) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.BottomStart
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            verticalArrangement = Arrangement.Bottom
        ) {
            Text(
                text = carouselItem.title,
                style = MaterialTheme.typography.displayMedium.copy(
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1
            )
            Text(
                text = carouselItem.description,
                style = MaterialTheme.typography.titleMedium.copy(
                    color = MaterialTheme.colorScheme.onSurface.copy(
                        alpha = 0.65f
                    ),
                    shadow = Shadow(
                        color = Color.Black.copy(alpha = 0.5f),
                        offset = Offset(x = 2f, y = 4f),
                        blurRadius = 2f
                    )
                ),
                maxLines = 1,
                modifier = Modifier.padding(top = 8.dp)
            )
            AnimatedVisibility(
                visible = isCarouselFocused,
                content = {
                    WatchNowButton()
                }
            )
        }
    }
}

@Composable
private fun CarouselItemBackground(carouselItem: CarouselItem , modifier: Modifier = Modifier) {
    Image(
        painter = carouselItem.imageUrl ,
        contentDescription = "",
        modifier = modifier
            .drawWithContent {
                drawContent()
                drawRect(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Transparent,
                            Color.Black.copy(alpha = 0.8f)
                        )
                    )
                )
            },
        contentScale = ContentScale.Crop
    )
}

@OptIn(ExperimentalTvMaterial3Api::class)
@Composable
private fun WatchNowButton() {
    Button(
        onClick = {},
        modifier = Modifier.padding(top = 8.dp),
        contentPadding = ButtonDefaults.ButtonWithIconContentPadding,
        shape = ButtonDefaults.shape(shape = JetStreamButtonShape),
        colors = ButtonDefaults.colors(
            containerColor = MaterialTheme.colorScheme.onSurface,
            contentColor = MaterialTheme.colorScheme.surface,
            focusedContentColor = MaterialTheme.colorScheme.surface,
        ),
        scale = ButtonDefaults.scale(scale = 1f)
    ) {
        Icon(
            imageVector = Icons.Outlined.PlayArrow,
            contentDescription = null,
        )
        Spacer(Modifier.size(8.dp))
        Text(
            text = "Watch Now",
            style = MaterialTheme.typography.titleSmall
        )
    }
}
