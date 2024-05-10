package io.github.curioustools.bookshub.uiactivities

import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.github.curioustools.bookshub.R
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.baseutils.BaseComposeActivity
import io.github.curioustools.bookshub.baseutils.ComposeUtils
import io.github.curioustools.bookshub.uiccomponents.IconModel
import io.github.curioustools.bookshub.uiccomponents.LoadingIcon
import io.github.curioustools.bookshub.uiccomponents.ScreenBar
import io.github.curioustools.bookshub.uimodels.Book
import io.github.curioustools.bookshub.uimodels.UserMetaInfo
import io.github.curioustools.bookshub.uiviewmodels.AuthViewModel
import io.github.curioustools.bookshub.uiviewmodels.BooksViewModel
import io.github.curioustools.bookshub.uiviewmodels.UserMetaViewModel

@AndroidEntryPoint
class DashboardActivity : BaseComposeActivity() {
    private val authViewModel by viewModels<AuthViewModel>()
    private val booksViewModel by viewModels<BooksViewModel>()
    private val metaViewModel by viewModels<UserMetaViewModel>()
    private var userMetaProfile :UserMetaInfo? = null

    override fun isEdgeToEdge() = false

    override fun afterUi() {
        super.afterUi()
        refresh()
    }

    @Composable
    @Preview
    fun Section(title: String = "ddd", content: List<Book> = Book.books,showGrid:Boolean=true, onClick: (Book) -> Unit = {}) {
        if(content.isEmpty()) return
        Column(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
            Text(
                text = title.replaceFirstChar { it.uppercaseChar() },
                style = ComposeUtils.Typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp)
            )
            if(showGrid){
                Column {
                    content.chunked(3).map {oneRowContent: List<Book> ->
                        Row(Modifier.fillMaxWidth()) {
                            oneRowContent.map {book ->
                                LoadingIcon(book.iconUrl, book, width = 120.dp, config = Modifier.padding(start = 8.dp, bottom = 8.dp)) { onClick.invoke(book) }
                            }
                        }
                    }
                }

            }
            else{
                LazyRow(Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
                    itemsIndexed(content) { i, book ->
                        Spacer(modifier = Modifier.size(12.dp))
                        LoadingIcon(book.iconUrl, book, width = 150.dp) { onClick.invoke(book) }
                    }
                }
            }


        }

    }


    @Composable
    @Preview
    override fun Ui() {
        val ctx = this@DashboardActivity
        var all = listOf(Book.loading, Book.loading, Book.loading, Book.loading,Book.loading)
        var reading = all
        var purchased = all
        var reviewed = all
        var bookmarked = all


        userMetaProfile = (metaViewModel.metaLiveData.observeAsState().value as? BaseResponse.Success)?.body
        if(userMetaProfile!=null) booksViewModel.getAllBooks()

        val booksState = booksViewModel.booksLiveData.observeAsState()
        when (val resp = booksState.value) {
            is BaseResponse.Failure -> toast(resp.exception.message.orEmpty())
            is BaseResponse.Loading -> {}
            is BaseResponse.Success -> {
                all = resp.body
                val profile = userMetaProfile
                reading = all.filter { bk -> bk.id in profile?.purchasedBookIds.orEmpty() }
                purchased = all.filter { bk -> bk.id in profile?.purchasedBookIds.orEmpty() }
                reviewed = all.filter { bk -> bk.id in profile?.reviewedBookIds.orEmpty() }
                bookmarked = all.filter { bk -> bk.id in profile?.bookmarkedBookIds.orEmpty() }
            }
            else -> {}
        }


        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {

            val userIcon = IconModel.CircleUrlModel(userMetaProfile?.profilePic.orEmpty()) {
                ProfileActivity.launch(ctx, userMetaProfile)
            }
            val title = userMetaProfile?.name?.let { "Hello, $it" }.orEmpty().ifEmpty { "Hello" }
            val search = IconModel.VectorModel(R.drawable.ic_search) {
                SearchActivity.launch(ctx, userMetaProfile)
            }
            ScreenBar(userIcon, listOf(search), title)


            Section("Currently Reading", reading) { BookDetailsActivity.launch(ctx, it, userMetaProfile) }
            Section("My Purchased Books", purchased) { BookDetailsActivity.launch(ctx, it, userMetaProfile) }
            Section("Books That I Reviewed", reviewed) { BookDetailsActivity.launch(ctx, it, userMetaProfile) }
            Section("My Bookmarks", bookmarked) { BookDetailsActivity.launch(ctx, it, userMetaProfile) }
            Section("People Are Reading", all, showGrid = true) { BookDetailsActivity.launch(ctx, it, userMetaProfile) }
        }

    }


    override fun onResume() {
        super.onResume()
        refresh()
    }

    private fun refresh() {
        val userid = authViewModel.getCurrentUserID()
        metaViewModel.getMetaInfo(userid)
    }
}

