package io.github.curioustools.bookshub.uiviewmodels

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import dagger.hilt.android.lifecycle.HiltViewModel
import io.github.curioustools.bookshub.base.BaseResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject


@HiltViewModel
class AuthViewModel @Inject constructor(private val firebaseAuth: FirebaseAuth) : ViewModel() {
    val signupLiveData = MutableLiveData<BaseResponse<String>>()
    val loginLiveData = MutableLiveData<BaseResponse<String>>()


    fun isLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }
    fun getCurrentUserID(): String {
        return firebaseAuth.currentUser?.uid.orEmpty()
    }

    fun logout(){
        firebaseAuth.signOut()
    }

    fun signup(email:String,password:String){
        viewModelScope.launch(Dispatchers.IO) {
           runCatching {
               signupLiveData.postValue(BaseResponse.Loading())
               val result: AuthResult? = firebaseAuth.createUserWithEmailAndPassword(email,password).await()
               val response:BaseResponse<String> = when{
                   result==null -> BaseResponse.basicError()
                   result.user==null ->BaseResponse.basicError()
                   else -> BaseResponse.Success(result.user?.uid.orEmpty())
               }
               signupLiveData.postValue(response)
           }.getOrElse {
               signupLiveData.postValue(BaseResponse.errorFromThrowable(it))
           }
        }

    }

    fun login(email:String,password:String){
        viewModelScope.launch(Dispatchers.IO) {
            kotlin.runCatching {
                loginLiveData.postValue(BaseResponse.Loading())
                val result: AuthResult? = firebaseAuth.signInWithEmailAndPassword(email,password).await()
                val response: BaseResponse<String> = when {
                    result == null -> BaseResponse.basicError()
                    result.user == null -> BaseResponse.userNotFoundError()
                    else -> BaseResponse.Success("Success")
                }
                loginLiveData.postValue(response)
            }.getOrElse {
                val response: BaseResponse<String> = when(it){
                    is FirebaseAuthInvalidCredentialsException -> BaseResponse.basicError("These doesn't seem to be valid credentials are you trying to create a new account?")
                    else ->BaseResponse.errorFromThrowable(it) // use firebase provided error
                }
                loginLiveData.postValue(response)
            }
        }

    }


}