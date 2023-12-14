package com.aquiles.hotel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aquiles.hotel.data.New
import com.aquiles.hotel.domain.GetNewsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class DashBoardViewModel : ViewModel() {
    val news = SnapshotStateList<New>()

     var isLoading by  mutableStateOf(true)
    val getNewsUseCase = GetNewsUseCase()
    var currentUrl  :String? = null

    init {
        getBreakingNews()
    }

    private fun getBreakingNews() {

        viewModelScope.launch( Dispatchers.IO) {
            val newList =  getNewsUseCase()

            withContext(Dispatchers.Main)
            {
                news.addAll(newList)
                isLoading = false
            }


        }
    }


}