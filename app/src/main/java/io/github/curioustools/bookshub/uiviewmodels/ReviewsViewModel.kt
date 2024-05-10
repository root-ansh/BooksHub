package io.github.curioustools.bookshub.uiviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.uimodels.Review
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class ReviewsViewModel @Inject constructor(private val firebaseDB: FirebaseDatabase): ViewModel(){
    private val table = FirebaseTableNames.REVIEWS.db

    val reviewsLiveData = MutableLiveData<BaseResponse<List<Review>>>()
    fun getReviews(reviewIDs:List<String>){
        viewModelScope.launch {
            runCatching {
                reviewsLiveData.postValue(BaseResponse.Loading())

                val reviewsDeferred: List<Deferred<Review?>> = reviewIDs.map {
                    async {
                        //todo warning: this get call makes a lot of costing on firebase.
                        // ensure to check cached db first by caching every profile via a listener in app
                        firebaseDB.getReference(table).child(it).get().await()
                            ?.getValue(Review::class.java)
                    }
                }
                val reviews = reviewsDeferred.awaitAll().filterNotNull()
                reviewsLiveData.postValue(BaseResponse.Success(reviews))
            }.getOrElse {
                it.printStackTrace()
                reviewsLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }

    }
    fun getAllReviews(){
        viewModelScope.launch {
            kotlin.runCatching {
                val allReviews: List<Review> = firebaseDB.reference.child(table).get()
                    .await().children.mapNotNull { it.getValue(Review::class.java) }
                reviewsLiveData.postValue(BaseResponse.Success(allReviews))
            }.getOrElse {
                it.printStackTrace()
                reviewsLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }
    }
    fun getAllBookReviews(bookId:String?){
        bookId?:return
        viewModelScope.launch {
            kotlin.runCatching {
                val allReviews: List<Review> = firebaseDB.reference.child(table).get()
                    .await().children.mapNotNull { it.getValue(Review::class.java) }
                val filtered = allReviews.filter {  it.bookId.equals(bookId,true) }
                reviewsLiveData.postValue(BaseResponse.Success(filtered))
            }.getOrElse {
                it.printStackTrace()
                reviewsLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }
    }



    val addReviewLiveData= MutableLiveData<BaseResponse<Review>>()
    fun addReview(review: Review){
        viewModelScope.launch {
            runCatching {
                firebaseDB.reference.child(table).child(review.id).setValue(review).await()
                addReviewLiveData.postValue(BaseResponse.Success(review))
            }.getOrElse {
                it.printStackTrace()
                addReviewLiveData.postValue(BaseResponse.errorFromThrowable(it))

            }
        }
    }

    val deleteReviewLiveData = MutableLiveData<Boolean>()
    fun deleteReview(review: Review){
        viewModelScope.launch {
            runCatching {
                firebaseDB.reference.child(table).child(review.id).removeValue().await()
                deleteReviewLiveData.postValue(true)
            }.getOrElse {
                    it.printStackTrace()
                deleteReviewLiveData.postValue(false)

            }
        }
    }

}