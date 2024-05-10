package io.github.curioustools.bookshub.api

import retrofit2.Response
import retrofit2.http.GET

interface ApiService {

    @GET(ApiDI.URL_QUIZ)
    suspend fun getQuiz():Response<List<QuizItem>>

}

