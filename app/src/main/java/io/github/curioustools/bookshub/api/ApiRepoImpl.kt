package io.github.curioustools.bookshub.api

import retrofit2.Response
import javax.inject.Inject

class ApiRepoImpl @Inject constructor(private val apiService: ApiService): ApiRepo {
    override suspend fun getQuiz(): Response<List<QuizItem>> {
        return apiService.getQuiz()
    }
}