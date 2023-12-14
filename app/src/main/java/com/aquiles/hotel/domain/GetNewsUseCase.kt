package com.aquiles.hotel.domain

import com.aquiles.hotel.data.New
import com.aquiles.hotel.data.Repository
import kotlinx.coroutines.flow.first

class GetNewsUseCase {

     suspend operator fun invoke() : List<New>
    {
          Repository.loadNews()
        val getSortedNewsUseCase = GetSortedNewsUseCase()

        return   getSortedNewsUseCase().first()
    }
}