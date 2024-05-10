package io.github.curioustools.bookshub.base

import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.activity.ComponentActivity

open class BaseActivity : ComponentActivity(){

    val  activityHandler by lazy { Handler(Looper.getMainLooper()) }

    fun toast(s:String){
        Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
    }

}