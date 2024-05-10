package io.github.curioustools.bookshub

import android.app.Application
import com.google.firebase.database.Logger
import dagger.hilt.android.HiltAndroidApp
import io.github.curioustools.bookshub.uiviewmodels.FirebaseDI

@HiltAndroidApp
class ComposeHubApp:Application() {

    override fun onCreate() {
        super.onCreate()
        if(BuildConfig.DEBUG) FirebaseDI.makeFirebaseDatabase(this).setLogLevel(Logger.Level.DEBUG)
    }
}