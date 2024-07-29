package com.example.mytv11

import android.util.Log
import com.example.composeconvert.Category
import com.example.composeconvert.R

enum class FilterType {
    ALL, RECENT, FAVORITE
}
lateinit var data : itemdata
var index : Int = 0

object DataCenter {
    val items1: List<itemdata> = listOf(
        itemdata("Nishant", R.drawable.naruto3, isRecent = true),
        itemdata("Item 2", R.drawable.naruto3, isRecent = true),
        itemdata("Item 3", R.drawable.naruto3 , isFavorite = true),
        itemdata("Item 4", R.drawable.naruto3, isFavorite = true)
    )
    val items2: List<itemdata> = listOf(
        itemdata("Item 1", R.drawable.naruto3),
        itemdata("Item 2", R.drawable.naruto3, isRecent = true),
        itemdata("Item 3", R.drawable.naruto3),
        itemdata("Item 4", R.drawable.naruto3, isFavorite = true)
    )
    val items3: List<itemdata> = listOf(
        itemdata("Item 1", R.drawable.ithachi),
        itemdata("Item 2", R.drawable.ithachi),
        itemdata("Item 3", R.drawable.ithachi, isRecent = true),
        itemdata("Item 4", R.drawable.ithachi),
        itemdata("Item 4", R.drawable.ithachi, isFavorite = true),
        itemdata("Item 4", R.drawable.ithachi),
        itemdata("Item 4", R.drawable.ithachi)
    )
    fun setData(item: itemdata) {
        data = item
    }
    fun getData():itemdata{
        return data
    }
    fun updateRecentStatus(item: itemdata) {
        item.isRecent = true
    }
    fun setIndex( inde : Int){
        index = inde
    }
    fun getIndex():Int{
        return index
    }
    fun updateFavStatus(item: itemdata) {
        item.isFavorite = true
    }
    fun updateFavStatusUn(item: itemdata) {
        item.isFavorite = false
    }

    val allCategories: List<Category> = listOf(
        Category("Category 1", items1),
        Category("Category 2", items2),
        Category("Category 3", items3)
    )

    fun getCategories(filterType: FilterType): List<Category> {
        return when (filterType) {
            FilterType.ALL -> allCategories
            FilterType.RECENT -> allCategories.mapNotNull { category ->
                val recentItems = category.items.filter { it.isRecent }
                if (recentItems.isNotEmpty()) {
                    Category(category.title, recentItems)
                } else {
                    null
                }
            }
            FilterType.FAVORITE -> allCategories.mapNotNull { category ->
                val favoriteItems = category.items.filter { it.isFavorite }
                if (favoriteItems.isNotEmpty()) {
                    Category(category.title, favoriteItems)
                } else {
                    null
                }
            }
        }
    }

    fun searchCategoriesByTitleInItems(title: String): List<Category> {
        if(title.equals("")){
            return listOf()
        }
        return allCategories.mapNotNull { category ->
            val filteredItems = category.items.filter { it.title.contains(title, ignoreCase = true) }
            if (filteredItems.isNotEmpty()) {
                Category(category.title, filteredItems)
            } else {
                null
            }
        }
    }

    fun initialize() {
        Log.e("nis", " hello")
    }


}
