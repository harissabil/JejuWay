package com.holidai.jejuway.di

import com.holidai.jejuway.data.network.weather.ApiService
import com.holidai.jejuway.data.network.weather.WeatherRepository
import okhttp3.OkHttpClient
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

fun provideHttpClient(): OkHttpClient {
    return OkHttpClient
        .Builder()
        .readTimeout(60, TimeUnit.SECONDS)
        .connectTimeout(60, TimeUnit.SECONDS)
        .build()
}


fun provideConverterFactory(): GsonConverterFactory =
    GsonConverterFactory.create()


fun provideRetrofit(
    okHttpClient: OkHttpClient,
    gsonConverterFactory: GsonConverterFactory,
): Retrofit {
    return Retrofit.Builder()
        .baseUrl("https://api.open-meteo.com/v1/")
        .client(okHttpClient)
        .addConverterFactory(gsonConverterFactory)
        .build()
}

fun provideService(retrofit: Retrofit): ApiService =
    retrofit.create(ApiService::class.java)

val weatherModule = module {
    single<OkHttpClient>(named("Weather")) { provideHttpClient() }
    single<GsonConverterFactory>(named("Weather")) { provideConverterFactory() }
    single<Retrofit>(named("Weather")) {
        provideRetrofit(
            get(named("Weather")),
            get(named("Weather"))
        )
    }
    single<ApiService>(named("Weather")) { provideService(get(named("Weather"))) }
}

val weatherRepositoryModule = module {
    single { WeatherRepository(get(named("Weather"))) }
}