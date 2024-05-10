package io.github.curioustools.bookshub.api

import retrofit2.Response

interface ApiRepo{
    suspend fun getQuiz():Response<List<QuizItem>>
}