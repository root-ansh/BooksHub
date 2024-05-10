package io.github.curioustools.bookshub.uiviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.uimodels.Book
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class BooksViewModel @Inject constructor(private val firebaseDB: FirebaseDatabase) : ViewModel() {
    private val table = FirebaseTableNames.BOOKS.db
    val booksLiveData = MutableLiveData<BaseResponse<List<Book>>>()
    fun getBooksById(bookIds: List<String>) {
        booksLiveData.postValue(BaseResponse.Loading())

        viewModelScope.launch {
            runCatching {
                val booksDeferred: List<Deferred<Book?>> = bookIds.map {
                    async {
                        firebaseDB.getReference(table).child(it).get().await()
                            ?.getValue(Book::class.java)
                    }
                }
                val books = booksDeferred.awaitAll().filterNotNull()
                booksLiveData.postValue(BaseResponse.Success(books))
            }.getOrElse {
                it.printStackTrace()
                booksLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }

    }

    fun getAllBooks() {
        viewModelScope.launch {
            kotlin.runCatching {
                val allBooks: List<Book> = firebaseDB.reference.child(table).get()
                    .await().children.mapNotNull { it.getValue(Book::class.java) }
                booksLiveData.postValue(BaseResponse.Success(allBooks))
            }.getOrElse {
                it.printStackTrace()
                booksLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }
    }

    val bookStatus = MutableLiveData<BaseResponse<BaseResponse<Book>>>()
    fun updateBook(book: Book){

    }

}


