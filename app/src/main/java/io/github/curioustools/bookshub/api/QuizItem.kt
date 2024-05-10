package io.github.curioustools.bookshub.api

import androidx.annotation.Keep
import androidx.compose.ui.graphics.Color

@Keep
data class QuizItem(
    val answer: String = "",
    val category: String = "",
    val choices: List<String> = listOf(),
    val question: String = ""
) {


   companion object{
       fun mock() = QuizItem(
           category = "",
           question = "Clouds are made up of these.",
           answer = "Water droplets and ice crystals",
           choices = listOf(
               "Carbon atoms",
               "Water droplets and ice crystals",
               "Oxygen ions",
               "Dust mites"
           ),
       )

       fun mockList() = mock().choices.map { it to Color.White }
   }
}