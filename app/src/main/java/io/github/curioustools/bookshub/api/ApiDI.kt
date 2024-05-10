package io.github.curioustools.bookshub.api

import android.content.Context
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object ApiDI{
    const val URL_BASE = "https://raw.githubusercontent.com/"
    const val URL_QUIZ = "/itmmckernan/triviaJSON/master/science-technology.json"
    @Provides
    fun makeHttpCache(@ApplicationContext context: Context): Cache {
        return Cache(context.applicationContext.cacheDir, 5 * 1024 * 1024L)
    }

    @Provides
    fun makeOkHttpClient(cache: Cache): OkHttpClient {
        val logger = HttpLoggingInterceptor().also { it.level = HttpLoggingInterceptor.Level.BODY }
        return OkHttpClient.Builder()
            .cache(cache)
            .retryOnConnectionFailure(true)
            .addInterceptor(logger)
            .connectTimeout(300, TimeUnit.SECONDS)
            .readTimeout(300, TimeUnit.SECONDS)
            .hostnameVerifier { _, _ -> true }
            .build()
    }

    @Provides
    fun makeGson(): Gson {
        return GsonBuilder().create()
    }

    @Provides
    fun makeRetrofitService(gson: Gson, okHttp: OkHttpClient) : Retrofit {
        val gsonFactory = GsonConverterFactory.create(gson)
        return Retrofit.Builder()
            .baseUrl(URL_BASE)
            .client(okHttp)
            .addConverterFactory(gsonFactory)
            .build()
    }


    @Provides
    fun makeQuizService(retrofit: Retrofit): ApiService {
        return retrofit.create(ApiService::class.java)
    }

    @Provides
    fun makeQuizRepo(service: ApiService): ApiRepo {
        return ApiRepoImpl(service)
    }

}