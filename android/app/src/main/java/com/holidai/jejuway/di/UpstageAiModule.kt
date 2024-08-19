package com.holidai.jejuway.di

import com.holidai.jejuway.BuildConfig
import com.holidai.jejuway.data.network.upstage_ai.ApiService
import com.holidai.jejuway.data.network.upstage_ai.UpstageAiRepository
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

fun provideUpstageAiLoggingInterceptor(): HttpLoggingInterceptor {
    return HttpLoggingInterceptor().apply {
        level = if (BuildConfig.DEBUG) {
            HttpLoggingInterceptor.Level.BODY
        } else {
            HttpLoggingInterceptor.Level.NONE
        }
    }
}

fun provideUpstageAiHttpClient(
    httpLoggingInterceptor: HttpLoggingInterceptor,
): OkHttpClient {
    return OkHttpClient
        .Builder()
        .addInterceptor(httpLoggingInterceptor)
        .connectTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .readTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .writeTimeout(90, java.util.concurrent.TimeUnit.SECONDS)
        .retryOnConnectionFailure(true)
        .build()
}


fun provideUpstageAiConverterFactory(): GsonConverterFactory =
    GsonConverterFactory.create()


fun provideUpstageAiRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.upstage.ai/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

fun provideUpstageAiService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

val upstageAiModule = module {
    single { provideUpstageAiLoggingInterceptor() }
    single { provideUpstageAiHttpClient(get()) }
    single { provideUpstageAiConverterFactory() }
    single { provideUpstageAiRetrofit(get(), get()) }
    single { provideUpstageAiService(get()) }
}

val upstageAiRepositoryModule = module {
    single { UpstageAiRepository(get()) }
}