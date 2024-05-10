package io.github.curioustools.bookshub.uiactivities

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.github.curioustools.bookshub.R
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.baseutils.BaseComposeActivity
import io.github.curioustools.bookshub.baseutils.MyComposeColors
import io.github.curioustools.bookshub.baseutils.roundOff
import io.github.curioustools.bookshub.uiccomponents.BText
import io.github.curioustools.bookshub.uiccomponents.BasicEditText
import io.github.curioustools.bookshub.uiccomponents.HText
import io.github.curioustools.bookshub.uiccomponents.IconModel
import io.github.curioustools.bookshub.uiccomponents.LoadingIcon
import io.github.curioustools.bookshub.uiccomponents.ReviewComponent
import io.github.curioustools.bookshub.uiccomponents.ScreenBar
import io.github.curioustools.bookshub.uiccomponents.SimpleRatingBar
import io.github.curioustools.bookshub.uiccomponents.basicTextStyle
import io.github.curioustools.bookshub.uimodels.Book
import io.github.curioustools.bookshub.uimodels.Review
import io.github.curioustools.bookshub.uimodels.UserMetaInfo
import io.github.curioustools.bookshub.uiviewmodels.ReviewsViewModel
import io.github.curioustools.bookshub.uiviewmodels.UserMetaViewModel
import java.util.UUID

@AndroidEntryPoint
class BookDetailsActivity : BaseComposeActivity() {
    companion object{
        fun launch(context: Context, profile: Book?,userProfile:UserMetaInfo?){
            curBook = profile
            user = userProfile
            context.startActivity(Intent(context,BookDetailsActivity::class.java))
        }
        private var curBook: Book? = null
        private var user:UserMetaInfo? = null
    }
    private val reviewsViewModel by viewModels<ReviewsViewModel>()
    private val userMetaViewModel by viewModels<UserMetaViewModel>()


    @Composable
    override fun Ui() {
        val bookChangeAble = remember { mutableStateOf(curBook) }
        val reviewsChangeable =  remember { mutableStateOf(listOf<Review>()) }
        val profileChangeable = remember { mutableStateOf(user) }
        when(val resp = reviewsViewModel.reviewsLiveData.observeAsState().value){
            is BaseResponse.Loading -> toggleLoading(true)
            is BaseResponse.Success -> {
                toggleLoading(false)
                reviewsChangeable.value = resp.body
            }
            else -> toggleLoading(false)
        }
        when(val resp = userMetaViewModel.metaUpdatedLiveData.observeAsState().value){
            is BaseResponse.Loading -> toggleLoading(true)
            is BaseResponse.Success -> {
                toggleLoading(false)
                profileChangeable.value = resp.body
            }
            else -> toggleLoading(false)
        }

        ActualUi(bookChangeAble.value,reviewsChangeable.value,profileChangeable.value)
    }

    override fun afterUi() {
        super.afterUi()
        reviewsViewModel.getAllBookReviews(curBook?.id)
        reviewsViewModel.addReviewLiveData.observe(this){resp ->
            when(resp){
                is BaseResponse.Loading -> toggleLoading(true)
                is BaseResponse.Success -> {
                    toggleLoading(false)
                    val reviewObj:Review =resp.body
                    toast("Review created successfully. id : ${reviewObj.id}")

                    val currBook = curBook?:return@observe
                    val user = BookDetailsActivity.user?:return@observe
                    val reviewedList = user.reviewedBookIds.orEmpty().toMutableList()
                    if(currBook.id !in reviewedList){
                        reviewedList.add(0,currBook.id)
                    }
                    val myReviews = user.myReviewsIds.orEmpty().toMutableList().also { it.add(reviewObj.id) }
                    user.reviewedBookIds = reviewedList
                    user.myReviewsIds = myReviews
                    userMetaViewModel.updateMetaInfo(user)
                    reviewsViewModel.getAllBookReviews(currBook.id)

                }
                else -> toggleLoading(false)
            }
        }

    }

    @Preview
    @Composable
    private fun ActualUi(book: Book? = Book.books.first(), reviewList: List<Review> = listOf(Review.mock, Review.mock), profile: UserMetaInfo?= UserMetaInfo.mock) {
        Column(Modifier.fillMaxWidth().verticalScroll(rememberScrollState())) {
            reviewList.forEach { it.setAuthorAsTitle() }
            val userBookmarks = profile?.bookmarkedBookIds.orEmpty()
            val userPurchasedBooks = profile?.purchasedBookIds.orEmpty()
            val curBookId = book?.id.orEmpty()
            val bookMarkRes = if(curBookId in userBookmarks)R.drawable.ic_bookmark else R.drawable.ic_bookmark_border
            val buttonText = if(curBookId in userPurchasedBooks  )"Read now" else "Download"

            ScreenBar(
                startIcon = IconModel.VectorModel(R.drawable.ic_back){onBackPressed()},
                endIcons = listOf(IconModel.VectorModel(bookMarkRes){toggleBookmark()}),
                title = ""
            )
            Row(Modifier.fillMaxWidth().padding(8.dp,12.dp)) {
                LoadingIcon(url = book?.iconUrl.orEmpty(), width = 120.dp, elevation = 0.dp)
                Column(Modifier.weight(1f)) {
                    HText(text = book?.name.orEmpty(), config = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp))
                    BText( text = "By ${book?.author.orEmpty()}", config = Modifier.fillMaxWidth().padding(bottom = 8.dp, start = 8.dp, end = 8.dp))
                    BText( text = "${book?.pageCount.orEmpty()} Pages", config = Modifier.fillMaxWidth().padding(bottom = 8.dp, start = 8.dp, end = 8.dp))
                    Row(Modifier.fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
                        val reviewsAvgRating = reviewList.map { it.rating.toFloatOrNull()?:0f }.average().toFloat()
                        val reviewCount = reviewList.size
                        SimpleRatingBar(rating = reviewsAvgRating, starConfig = Modifier.size(16.dp), fillColor = MyComposeColors.Black, unFillColor = MyComposeColors.DarkGrey)
                        BText( text = "$reviewsAvgRating ($reviewCount Reviews) ", config = Modifier.weight(1f).padding(start = 8.dp))
                    }
                }
            }
            book?.desc?.let {
                HText(text = "About This Book", config = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp), additionalStyling = basicTextStyle(4,true))
                BText(text = it, config = Modifier.padding(horizontal = 8.dp))
            }
            Button(modifier = Modifier.fillMaxWidth().padding(8.dp), onClick = {onDownloadPress()}) {
                Text(text = buttonText.uppercase())
            }
            reviewList.let { r ->
                if(r.isNotEmpty()){
                    HText(text = "User Reviews", config = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp), additionalStyling = basicTextStyle(4,true))
                    r.map {
                        ReviewComponent(review = it, showDelete = false, showBook = false, config = Modifier.padding(start = 16.dp, end = 16.dp, bottom = 8.dp))
                    }
                }

            }
            HText(text = "Add your Review", config = Modifier.fillMaxWidth().padding(vertical = 8.dp, horizontal = 8.dp), additionalStyling = basicTextStyle(4,true))

            var currentReview = ""
            val sliderPosition = remember { mutableFloatStateOf(0.0f) }
            Row(Modifier.padding(horizontal = 8.dp).fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {

                Slider(
                    modifier = Modifier.weight(1f),
                    value = sliderPosition.floatValue,
                    onValueChange = { sliderPosition.floatValue = it },
                    steps = 9,
                    valueRange = 0f..5f
                )
                Text(text = sliderPosition.floatValue.roundOff(3))
            }
            BasicEditText(title = "Your Review", modifier = Modifier.padding(8.dp)){
                currentReview = it
            }
            OutlinedButton(modifier = Modifier.fillMaxWidth().padding(horizontal = 8.dp), onClick = {
                addReview(
                    review = currentReview,
                    rating = sliderPosition.floatValue.roundOff(3).toFloatOrNull()?:0f,
                    userMetaInfo = profile,
                    currBook = book

                )
            }) {
                Text(text = "Add Review".uppercase())
            }
            Spacer(modifier = Modifier.size(48.dp))
        }

    }

    private fun onDownloadPress() {
        val bookid = curBook?.id.orEmpty()
        val userBooks = user?.purchasedBookIds.orEmpty().toMutableList()
        if(bookid !in userBooks){
            userBooks.add(bookid)
            user?.purchasedBookIds = userBooks
            userMetaViewModel.updateMetaInfo(user)
        }
    }

    private fun toggleBookmark() {
        val currBookId = curBook?.id.orEmpty()
        val userBookMarkedBooks = user?.bookmarkedBookIds.orEmpty()
        val bookInUserBookmarks = userBookMarkedBooks.firstOrNull { it.equals(currBookId,true) }!=null
        if(bookInUserBookmarks){
            user?.bookmarkedBookIds = userBookMarkedBooks.filter { !it.equals(currBookId,true) }
        }
        else {
            val newList = userBookMarkedBooks.toMutableList().also { it.add(0,currBookId) }
            user?.bookmarkedBookIds = newList
        }
        userMetaViewModel.updateMetaInfo(user)
    }

    private fun addReview(review:String,rating:Float,userMetaInfo: UserMetaInfo?,currBook: Book?){
        userMetaInfo?:return
        currBook?:return
        val reviewObj = Review(
            title = currBook.name,
            desc = review,
            id = UUID.randomUUID().toString(),
            rating = rating.toString(),
            creatorName = userMetaInfo.name.orEmpty(),
            creatorImageUrl = userMetaInfo.profilePic.orEmpty(),
            creatorId = userMetaInfo.userId.orEmpty(),
            bookId = currBook.id
        )
        reviewsViewModel.addReview(reviewObj)


    }
}