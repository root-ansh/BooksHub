package io.github.curioustools.bookshub.uiactivities

import android.content.Context
import android.content.Intent
import android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK
import android.content.Intent.FLAG_ACTIVITY_NEW_TASK
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.github.curioustools.bookshub.R
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.baseutils.BaseComposeActivity
import io.github.curioustools.bookshub.baseutils.MyComposeColors
import io.github.curioustools.bookshub.baseutils.getIconUrl
import io.github.curioustools.bookshub.baseutils.getIdFromUrl
import io.github.curioustools.bookshub.uiccomponents.BasicEditText
import io.github.curioustools.bookshub.uiccomponents.BasicIcon
import io.github.curioustools.bookshub.uiccomponents.HText
import io.github.curioustools.bookshub.uiccomponents.IconModel
import io.github.curioustools.bookshub.uiccomponents.ReviewComponent
import io.github.curioustools.bookshub.uiccomponents.ScreenBar
import io.github.curioustools.bookshub.uimodels.Review
import io.github.curioustools.bookshub.uimodels.UserMetaInfo
import io.github.curioustools.bookshub.uiviewmodels.AuthViewModel
import io.github.curioustools.bookshub.uiviewmodels.ReviewsViewModel
import io.github.curioustools.bookshub.uiviewmodels.UserMetaViewModel

@AndroidEntryPoint
class ProfileActivity : BaseComposeActivity() {
    companion object{
        private var curProfile: UserMetaInfo? = null
        fun launch(context: Context,profile:UserMetaInfo?){
            curProfile = profile
            context.startActivity(Intent(context,ProfileActivity::class.java))
        }
    }
    private val authVM by viewModels<AuthViewModel>()
    private val metaInfoVM by viewModels<UserMetaViewModel>()
    private val reviewViewModel by viewModels<ReviewsViewModel>()

    @Preview
    @Composable
    override fun Ui() {
        val reviewsChangeable = remember { mutableStateOf<List<Review>>(listOf()) }

        val reviewRespObserver = reviewViewModel.reviewsLiveData.observeAsState()
        when(val resp = reviewRespObserver.value){
            is BaseResponse.Failure -> toggleLoading(false)
            is BaseResponse.Loading -> {toggleLoading(true)}
            is BaseResponse.Success -> {
                toggleLoading(false)
                reviewsChangeable.value = resp.body
            }
            null -> {}
        }

        Column(modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()), horizontalAlignment = Alignment.CenterHorizontally) {
            val profile = remember { mutableStateOf(curProfile) }
            val icon: MutableState<String> = remember { mutableStateOf(profile.value?.profilePic.orEmpty()) }


            ScreenBar(
                startIcon = IconModel.VectorModel(R.drawable.ic_back){onBackPressed()},
                endIcons = listOf(IconModel.ButtonIcon("Logout"){logout()}),
                title = ""
            )
            BasicIcon(
                config = Modifier.padding(24.dp),
                model = IconModel.CircleUrlModel(icon.value),
                size = 120.dp,
                bg = MyComposeColors.Green
            )
            HText(text = "Your Profile Details", config = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth())
            BasicEditText(Modifier.padding(16.dp,8.dp), getIdFromUrl(icon.value),"Profile Pic", KeyboardType.Number){ newUrl ->
                profile.value?.profilePic = getIconUrl(newUrl)
                icon.value = getIconUrl(newUrl)
            }
            BasicEditText(Modifier.padding(16.dp,8.dp),profile.value?.name.orEmpty(),"User Name", KeyboardType.Text){newUrl ->
                profile.value?.name = newUrl
            }
            BasicIcon(
                model = IconModel.ButtonIcon("Update User Details") {
                    updateProfile(profile.value)
                },
                config = Modifier.padding(12.dp).fillMaxWidth()
            )
            HText(text = "Your Reviews", config = Modifier.padding(horizontal = 16.dp, vertical = 8.dp).fillMaxWidth())
            reviewsChangeable.value.map {r->
                ReviewComponent(r,Modifier.padding(horizontal = 16.dp),true){
                    onDelete(r)
                }
                Spacer(modifier = Modifier.size(18.dp))
            }


        }

    }

    override fun afterUi() {
        super.afterUi()
        // init data
        reviewViewModel.getReviews(curProfile?.myReviewsIds.orEmpty())
        reviewViewModel.deleteReviewLiveData.observe(this){
            reviewViewModel.getReviews(curProfile?.myReviewsIds.orEmpty())
        }
    }

    private fun updateProfile(newP: UserMetaInfo?) {
        metaInfoVM.updateMetaInfo(newP)
        finish()
    }

    private fun logout() {
        authVM.logout()
        val intent = Intent(this@ProfileActivity,AuthActivity::class.java)
        intent.flags = FLAG_ACTIVITY_NEW_TASK or FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
    }

    private fun onDelete(review: Review) {
        reviewViewModel.deleteReview(review)
    }

}