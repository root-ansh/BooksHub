package io.github.curioustools.bookshub.uiviewmodels

import android.content.Context
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.Logger
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.curioustools.bookshub.BuildConfig

@Module
@InstallIn(SingletonComponent::class)
object FirebaseDI {

    @Provides
    fun makeFirebaseAuth(@ApplicationContext context: Context): FirebaseAuth {
        return Firebase.auth
    }

    @Provides
    fun makeFirebaseDatabase(@ApplicationContext context: Context): FirebaseDatabase {
        return Firebase.database
    }

}