package com.example.composeconvert

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.ui.graphics.vector.ImageVector

enum class Screens(
    private val args: List<String>? = null,
    val isTabItem: Boolean = false,
    val tabIcon: ImageVector? = null
)
{
    SpashScreen,
    All(isTabItem = true),
    Categories(isTabItem = true),
    Favourites(isTabItem = true),
    Recent(isTabItem = true),
    Search(isTabItem = true, tabIcon = Icons.Default.Search),
    CategoryMovieList(listOf("categoryId")),
    MovieDetails(listOf("movieId")),
    Dashboard,
    VideoPlayer(listOf("movieId"));

    operator fun invoke(): String {
        val argList = StringBuilder()
        args?.let { nnArgs ->
            nnArgs.forEach { arg -> argList.append("/{$arg}") }
        }
        return name + argList
    }

    fun withArgs(vararg args: Any): String {
        val destination = StringBuilder()
        args.forEach { arg -> destination.append("/$arg") }
        return name + destination
    }

}