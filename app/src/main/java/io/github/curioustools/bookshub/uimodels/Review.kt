package io.github.curioustools.bookshub.uimodels

import androidx.annotation.Keep
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import io.github.curioustools.bookshub.baseutils.lipsum
import java.io.Serializable


@Keep
data class Review(
    val id: String = "",
    var title: String = "",
    val desc: String = "",
    val rating: String = "",
    val creatorName: String = "",
    val creatorImageUrl: String = "",
    val creatorId: String = "",
    val bookId: String = "",
    val bookImageUrl: String = ""
) : Serializable {
    fun setAuthorAsTitle(){
        title = creatorName
    }
    companion object {
        val loading = Review("loading")
        val mock = Review(
            "1",
            "Review Title",
            desc = "Description: ${lipsum(20)}",
            rating = "3",
            creatorName = "Reyna Doyle",
            creatorImageUrl = "https://picsum.photos/id/134/340",
            creatorId = "cetero",
            bookId = "dolores",
            bookImageUrl = "https://picsum.photos/id/134/340"

        )
    }
}

@Keep
data class UserMetaInfo(
    var userId: String? = null,
    var name: String? = null,
    var profilePic: String? = null,
    var purchasedBookIds: List<String>? = null,
    var reviewedBookIds: List<String>? = null,
    var bookmarkedBookIds: List<String>? = null,
    var myReviewsIds:List<String>? = null
) : Serializable {
    companion object {
        val loading = UserMetaInfo("loading")
        val mock = UserMetaInfo(
            userId = "1234",
            name = "Johnny",
            profilePic = "https://picsum.photos/id/237/200/",
            purchasedBookIds = listOf("1","2","1","2","1","2"),
            reviewedBookIds = listOf("1","2","1","2","1","2"),
            bookmarkedBookIds = listOf("1","2","1","2","1","2"),
            myReviewsIds = listOf("1","2","1","2","1","2")

        )
        fun new(id:String) = UserMetaInfo(
            userId = id,
            name = "New User",
            profilePic = "https://picsum.photos/id/101/200/",
            purchasedBookIds = listOf(),
            reviewedBookIds = listOf(),
            bookmarkedBookIds = listOf(),
            myReviewsIds = listOf()

        )
    }
}

@Keep
data class Book(
    val id:String = "",
    val name: String = "",
    val author: String = "",
    val pageCount: String = "",
    val desc: String = "",
    val iconUrl: String = "",
) : Serializable {

    companion object {
        val loading = Book(iconUrl = "https://x.y.z/pic.jpg")
        val books = listOf(
            Book(iconUrl = "https://picsum.photos/id/237/200/", name = "The Great Gatsby", id = "1", author = "Bilal Saeed", pageCount = "250", desc = LoremIpsum(50).values.joinToString(), ),
            Book(iconUrl = "https://picsum.photos/id/236/200/", name = "title"),
            Book(iconUrl = "https://picsum.photos/id/235/200/", name = "title"),
            Book(iconUrl = "https://picsum.photos/id/234/200/", name = "title"),
            Book(iconUrl = "https://picsum.photos/id/234/200/", name = "title"),
            Book(iconUrl = "https://picsum.photos/id/233/200/", name = "title"),
            Book(iconUrl = "https://picsum.photos/id/232/200/", name = "title"),

            )
    }
}