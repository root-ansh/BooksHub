package io.github.curioustools.bookshub.uiactivities

import android.content.Intent
import androidx.activity.viewModels
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dagger.hilt.android.AndroidEntryPoint
import io.github.curioustools.bookshub.base.BaseResponse
import io.github.curioustools.bookshub.baseutils.BaseComposeActivity
import io.github.curioustools.bookshub.uiccomponents.AppIcon
import io.github.curioustools.bookshub.uiccomponents.BasicEditText
import io.github.curioustools.bookshub.uiccomponents.SelectionButtonsOneByTwo
import io.github.curioustools.bookshub.uimodels.UserMetaInfo
import io.github.curioustools.bookshub.uiviewmodels.AuthViewModel
import io.github.curioustools.bookshub.uiviewmodels.UserMetaViewModel


@AndroidEntryPoint
class AuthActivity : BaseComposeActivity() {

    private val authViewModel by viewModels<AuthViewModel>()
    private val userMetaViewModel by viewModels<UserMetaViewModel>()

    override fun isEdgeToEdge(): Boolean {
        return true
    }

    override fun beforeUi() {
        super.beforeUi()
        val onLoginSignup = {it:BaseResponse<String> ->
            when(it){
                is BaseResponse.Failure -> toast(it.exception.message.orEmpty())
                is BaseResponse.Success -> {
                    toast("success")
                    finish()
                    startActivity(Intent(this,DashboardActivity::class.java))
                }
                else ->{}
            }
        }
        val onSignup = { it:BaseResponse<String> ->
            when(it){
                is BaseResponse.Failure -> toast(it.exception.message.orEmpty())
                is BaseResponse.Success -> {
                    toast("success")
                    val userID = it.body
                    userMetaViewModel.updateMetaInfo(UserMetaInfo.new(userID),userID)
                }
                else ->{}
            }
        }
        authViewModel.loginLiveData.observe(this,onLoginSignup)
        authViewModel.signupLiveData.observe(this,onSignup)
        userMetaViewModel.metaUpdatedLiveData.observe(this){
            when(it){
                is BaseResponse.Failure -> toast(it.exception.message.orEmpty())
                is BaseResponse.Success -> {
                    toast("success")
                    finish()
                    startActivity(Intent(this,DashboardActivity::class.java))
                }
                else ->{}
            }
        }
    }


    @Composable @Preview
    override fun Ui() {
        var isLogin = true
        var email = ""
        var pwd = ""
        Column(modifier = Modifier.fillMaxSize().padding(vertical = 48.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            AppIcon(elevation = 0.dp, rounding = 80.dp, border = BorderStroke(0.dp, Color.White))
            SelectionButtonsOneByTwo(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                items = listOf("Login","Signup"),
                default = "Login",
                onClick = { isLogin = it.equals("Login",true) }
            )

            BasicEditText(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                onValueChanged = { email = it }
            )

            BasicEditText(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 18.dp),
                inputType = KeyboardType.Password,
                title = "Password",
                onValueChanged = { pwd = it }
            )

            Button(
                onClick = {
                    if(isLogin) authViewModel.login(email,pwd)
                    else authViewModel.signup(email,pwd)
                },
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                Text(text = "Submit")
            }


        }

    }




}