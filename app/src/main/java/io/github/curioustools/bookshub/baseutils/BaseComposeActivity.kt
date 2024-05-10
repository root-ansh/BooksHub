package io.github.curioustools.bookshub.baseutils

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.MutableLiveData
import io.github.curioustools.bookshub.uiccomponents.LoadingUI

abstract class BaseComposeActivity : ComponentActivity() {
    private var onCreateBundle: Bundle? = null


    private var align = Alignment.Center
    private var margin = 0
    private val showLoading = MutableLiveData<Boolean>(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        onCreateBundle = savedInstanceState
        beforeUi()
        setContent {
            ComposeUtils.AppTheme {
                val loadingState =  showLoading.observeAsState(false)

                Scaffold {
                    Box(modifier = Modifier.padding(if(isEdgeToEdge()) PaddingValues(0.dp) else it).fillMaxSize()) {
                        Ui()
                        if (loadingState.value == true) {
                            LoadingUI(modifier = Modifier.padding(margin.dp).align(align))
                        }

                    }
                }
            }

        }
        afterUi()
    }

    fun getOnCreateBundle() = onCreateBundle


    fun toggleLoading(loading: Boolean,alignment :Alignment = Alignment.Center, margins:Int= 0) {
        showLoading.value = loading
        align = alignment
        margin = margins
    }

    open fun beforeUi() {}
    open fun afterUi() {}


    @Composable
    abstract fun Ui()

    open fun isEdgeToEdge(): Boolean {return false}


    val activityHandler by lazy { Handler(Looper.getMainLooper()) }

    fun toast(s: String) {
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }
}