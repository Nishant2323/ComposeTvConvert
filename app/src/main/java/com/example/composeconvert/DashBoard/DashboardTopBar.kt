package com.example.composeconvert.DashBoard

import android.util.Log
import androidx.compose.foundation.focusGroup
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusProperties
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.focusRestorer
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.input.key.Key
import androidx.compose.ui.input.key.KeyEventType
import androidx.compose.ui.input.key.key
import androidx.compose.ui.input.key.onKeyEvent
import androidx.compose.ui.input.key.type
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.tv.material3.Icon
import androidx.tv.material3.LocalContentColor
import androidx.tv.material3.MaterialTheme
import androidx.tv.material3.Tab
import androidx.tv.material3.TabRow
import androidx.tv.material3.Text
import com.example.composeconvert.Banner
import com.example.composeconvert.JetStreamCardShape
import com.example.composeconvert.Screens
import kotlinx.coroutines.launch

val TopBarTabs = Screens.entries.toList().filter { it.isTabItem }
val TopBarFocusRequesters = List(size = TopBarTabs.size) { FocusRequester() }



@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DashboardTopBar(
    lazyColum:FocusRequester,
    lazyRow: FocusRequester,
    changed: (ischanged:Boolean) -> Unit,
    navControllerBanner: NavController,
    carousefocusrequester: FocusRequester,
    isComingBackFromDifferentScreen:Boolean,
    resetIsComingBackFromDifferentScreen:()-> Unit,
    iscomingbackformmovielist: Boolean,
    lazyListState: LazyListState,
    modifier: Modifier = Modifier,
    selectedTabIndex: Int,
    screens: List<Screens> = TopBarTabs,
    focusRequesters: List<FocusRequester> = remember { TopBarFocusRequesters },
    onScreenSelection: (screen: Screens) -> Unit,
){
    val coroutineScope = rememberCoroutineScope()
    var  padding = rememberChildPadding()
    val focusManager = LocalFocusManager.current
    var isTabRowFocused by remember { mutableStateOf(false) }


    Row(modifier = modifier
        .focusGroup()
        .focusRestorer()
        .padding(padding.end,padding.top,0.dp,padding.bottom) , verticalAlignment = Alignment.CenterVertically)
    {
        TabRow(
            modifier = Modifier.fillMaxWidth()
                .onFocusChanged {
                    isTabRowFocused = it.isFocused || it.hasFocus
                },
            selectedTabIndex = selectedTabIndex,
            indicator = { tabPositions, _ ->
                if (selectedTabIndex >= 0) {
                    DashboardTopBarItemIndicator(
                        currentTabPosition = tabPositions[selectedTabIndex],
                        anyTabFocused = isTabRowFocused,
                        shape = JetStreamCardShape
                    )
                }
            },
            separator = { Spacer(modifier = Modifier.width(80.dp)) }
        ){
            screens.forEachIndexed { index, screen ->
                val itemModifier = if (index == 0) {
                    Modifier.height(32.dp)
                        .focusRequester(focusRequesters[index])
                        .focusProperties { down = carousefocusrequester }
                        .onFocusChanged { coroutineScope.launch { if(it.isFocused)  lazyListState.scrollToItem(0)}
                            Log.e("nish2","All is ${it.isFocused}")
                           if(it.isFocused){

                               if(iscomingbackformmovielist){navControllerBanner.navigate(Banner.Carusole())
                                   changed(false) }

                               if(isComingBackFromDifferentScreen){
//                                   navControllerBanner.navigate(Banner.Carusole())
                               }
                           }
                        }
                } else if(index==3){
                    Modifier.height(32.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.DirectionDown && keyEvent.type == KeyEventType.KeyUp) {
                                Log.e("nish6", "in Index0")
                                coroutineScope.launch {
                                    lazyColum.requestFocus()
                                }

                                true // Indicate that the event was handled
                            } else {
                                false // Allow other handlers to process this event
                            }

                        }
                        .onFocusChanged {
                            if(it.isFocused){

                            }
                            Log.e("nish22","recent is ${it.isFocused}")

                        }
                }
                else if(index==2){
                    Modifier.height(32.dp)
                        .focusRequester(focusRequesters[index])
                        .onKeyEvent { keyEvent ->
                            if (keyEvent.key == Key.DirectionDown && keyEvent.type == KeyEventType.KeyUp) {
                                Log.e("nish6", "in Index0")
                                coroutineScope.launch {
                                    lazyColum.requestFocus()
                                }

                                true // Indicate that the event was handled
                            } else {
                                false // Allow other handlers to process this event
                            }

                        }
                        .onFocusChanged {
                            if(it.isFocused){

                            }
                            Log.e("nish22","recent is ${it.isFocused}")

                        }
                }

                else {
                    Modifier.height(32.dp).focusRequester(focusRequesters[index])
                }
                key(index) {

                    Tab(
                        modifier = itemModifier,
                        selected = index == selectedTabIndex,
                        onFocus = { onScreenSelection(screen) },
                        onClick = { },
                    ) {
                        if (screen.tabIcon != null) {
                            Icon(
                                screen.tabIcon,
                                modifier = Modifier.padding(4.dp),
                                contentDescription = "dashboard Icon",
                                tint = LocalContentColor.current
                            )
                        } else {
                            Text(
                                modifier = Modifier
                                    .padding(horizontal = 16.dp),
                                text = screen(),
                                fontSize = 20.sp,
                                style = MaterialTheme.typography.titleSmall.copy(
                                    color = LocalContentColor.current
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}