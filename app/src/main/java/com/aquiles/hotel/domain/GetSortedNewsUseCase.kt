package com.aquiles.hotel.domain

import com.aquiles.hotel.data.Repository
import kotlinx.coroutines.flow.flow


class GetSortedNewsUseCase {


    suspend  operator  fun  invoke() = flow {
        while (true){
            val news = Repository.getNews()
            if (news.isEmpty()){

                kotlinx.coroutines.delay(1000)
                continue
            }
            else{
                emit(news)

            }

        }

    }
}