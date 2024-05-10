package io.github.curioustools.bookshub.uiactivities

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.os.postDelayed
import dagger.hilt.android.AndroidEntryPoint
import io.github.curioustools.bookshub.baseutils.BaseComposeActivity
import io.github.curioustools.bookshub.baseutils.MyComposeColors
import io.github.curioustools.bookshub.uiccomponents.AppIcon
import io.github.curioustools.bookshub.uiviewmodels.AuthViewModel


@AndroidEntryPoint
class SplashActivity : BaseComposeActivity() {
    private val authVM by viewModels<AuthViewModel>()


    override fun isEdgeToEdge(): Boolean {
        return true
    }
    
    override fun beforeUi() {
        super.beforeUi()
        toggleLoading(true, Alignment.BottomCenter,60)
        activityHandler.postDelayed(1800){
            if(authVM.isLoggedIn()){
                startActivity(Intent(this,DashboardActivity::class.java))
            }
            else{
                startActivity(Intent(this,AuthActivity::class.java))
            }
            finish()
        }
    }

    @Composable @Preview
    override fun Ui() {
        Box(modifier = Modifier.fillMaxSize().background(MyComposeColors.Green)) {
            AppIcon(modifier = Modifier.padding(vertical = 16.dp).align(Alignment.Center))
        }
    }



}