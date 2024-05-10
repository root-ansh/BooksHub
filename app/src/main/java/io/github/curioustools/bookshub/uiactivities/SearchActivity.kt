package io.github.curioustools.bookshub.uiactivities

import android.content.Context
import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.github.curioustools.bookshub.R
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.baseutils.BaseComposeActivity
import io.github.curioustools.bookshub.uiccomponents.BasicEditText
import io.github.curioustools.bookshub.uiccomponents.IconAndTitle
import io.github.curioustools.bookshub.uiccomponents.IconModel
import io.github.curioustools.bookshub.uiccomponents.ScreenBar
import io.github.curioustools.bookshub.uimodels.Book
import io.github.curioustools.bookshub.uimodels.UserMetaInfo
import io.github.curioustools.bookshub.uiviewmodels.BooksViewModel

@AndroidEntryPoint
class SearchActivity : BaseComposeActivity() {
    companion object{
        private var user:UserMetaInfo? = null
        fun launch(context: Context, userProfile: UserMetaInfo?){
            user = userProfile
            context.startActivity(Intent(context,SearchActivity::class.java))
        }
    }

    private val booksViewModel by viewModels<BooksViewModel>()

    @Preview
    @Composable
    override fun Ui() {
        val ctx = this@SearchActivity
        //val origBookList = mutableStateOf(BaseResponse.Success(Book.books))
        val currentBooks: MutableState<List<Book>> = remember { mutableStateOf(listOf()) }
        var origBookList = listOf<Book>()
        val origBookState = booksViewModel.booksLiveData.observeAsState()
        when(val state = origBookState.value){
            is BaseResponse.Failure -> {toggleLoading(false)}
            is BaseResponse.Loading -> {toggleLoading(true)}
            is BaseResponse.Success -> {
                toggleLoading(false)
                currentBooks.value =state.body
                origBookList = state.body
            }
            null -> {toggleLoading(false)}
        }

        Column(modifier = Modifier.fillMaxSize()) {
            ScreenBar(startIcon = IconModel.VectorModel(R.drawable.ic_back){onBackPressed()}, endIcons = listOf(), title = "")
            BasicEditText(modifier = Modifier.padding(8.dp), title = "Search", inputType = KeyboardType.Text){searchString ->
                currentBooks.value = filterData(origBookList,searchString)
            }

            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                modifier =  Modifier.fillMaxWidth().padding( 16.dp),
            ) {
                itemsIndexed(currentBooks.value) { i, book ->
                    IconAndTitle(
                        url = book.iconUrl,
                        title = book.name,
                        margins = PaddingValues(end = 16.dp, bottom = 16.dp)
                    ){
                        BookDetailsActivity.launch(ctx, book, user)
                    }

                }
            }

        }

    }

    override fun afterUi() {
        super.afterUi()
        booksViewModel.getAllBooks()
    }

    fun filterData(currentList:List<Book>,search: String):List<Book> {
        val titleResults = currentList.filter { it.name.contains(search,true) }
        val authorResults = currentList.filter { it.author.contains(search,true) }
        val descResult = currentList.filter { it.desc.contains(search,true) }
        val final = mutableSetOf<Book>()
        final.addAll(titleResults)
        final.addAll(authorResults)
        final.addAll(descResult)
        val new = final.toList()
        return new//.ifEmpty { currentList }
    }


}