package com.example.renessans7.module.network

import com.example.renessans7.module.service.ApiService
import com.example.renessans7.utils.Constants.AUTHORIZATION
import com.example.renessans7.utils.Constants.BASE_URL
import com.example.renessans7.utils.Constants.BEARER
import com.example.renessans7.utils.SharedPref
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class NetworkModule {

    private var gson: Gson = GsonBuilder()
        .setLenient()
        .create()

    @Provides
    @Singleton
    fun getRetrofit(client: OkHttpClient): Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(client)
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @Singleton
    fun getApiService(retrofit: Retrofit): ApiService =
        retrofit.create(ApiService::class.java)

    @Provides
    @Singleton
    fun getClient(sharedPref: SharedPref): OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(100, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .addInterceptor(HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        })
        .addInterceptor(Interceptor { chain ->
            val builder = chain.request().newBuilder()
            if (sharedPref.token.isNotEmpty()) {
                builder.addHeader(
                    AUTHORIZATION,
                    "$BEARER${sharedPref.token}"
                )
            }
            chain.proceed(builder.build())
        })
        .build()
}