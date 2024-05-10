package io.github.curioustools.bookshub.uiviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.FirebaseDatabase
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.base.BaseStatus
import io.github.curioustools.bookshub.uimodels.UserMetaInfo
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class UserMetaViewModel @Inject constructor(private val firebaseDB: FirebaseDatabase): ViewModel(){
    private val metaTable = FirebaseTableNames.USER_META.db
    val metaUpdatedLiveData = MutableLiveData<BaseResponse<UserMetaInfo>>()

    fun updateMetaInfo(info: UserMetaInfo?,id:String?= info?.userId.orEmpty()){
        info?:return
        viewModelScope.launch {
            runCatching {
                metaUpdatedLiveData.postValue(BaseResponse.Loading())
                firebaseDB.getReference(metaTable).child(id.orEmpty()).setValue(info).await()
                metaUpdatedLiveData.postValue(BaseResponse.Success(info))
            }.getOrElse {
                it.printStackTrace()
                metaUpdatedLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }

    }

    val metaLiveData = MutableLiveData<BaseResponse<UserMetaInfo>>()
    fun getMetaInfo(userId:String){
        viewModelScope.launch {
            runCatching {
                metaLiveData.postValue(BaseResponse.Loading())
                //todo warning: this get call makes a lot of costing on firebase.
                // ensure to check cached db first by caching every profile via a listener in app
                val ref: DataSnapshot? = firebaseDB.getReference(metaTable).child(userId).get().await()
                val response: BaseResponse<UserMetaInfo> = when {
                    ref == null -> BaseResponse.basicError()
                    !ref.exists() -> BaseStatus.SERVER_DOWN_502.asResponse()
                    else -> {
                        val data = ref.getValue(UserMetaInfo::class.java)!!
                        BaseResponse.Success(data)
                    }
                }
                metaLiveData.postValue(response)
            }.getOrElse {
                it.printStackTrace()
                metaLiveData.postValue(BaseResponse.errorFromThrowable(it))
            }
        }

    }

}