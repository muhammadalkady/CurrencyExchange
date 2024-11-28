package com.kady.muhammad.currencyexchange.di.exchange

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import com.kady.muhammad.currencyexchange.BuildConfig
import com.kady.muhammad.exchange.network.interceptors.ApiKeyInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.MediaType
import okhttp3.OkHttpClient
import retrofit2.Converter
import retrofit2.Retrofit
import javax.inject.Named

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    private const val BASE_URL = "BASE_URL"
    private const val API_KEY = "API_KEY"
    private val json = Json {
        ignoreUnknownKeys = true
    }

    @Provides
    fun providesOkHttpClient(apiKeyInterceptor: ApiKeyInterceptor): OkHttpClient =
        OkHttpClient.Builder().addInterceptor(apiKeyInterceptor).build()

    @Provides
    fun providesApiKeyInterceptor(@Named(API_KEY) apiKey: String) = ApiKeyInterceptor(apiKey)


    @Provides
    @Named(API_KEY)
    fun providesApiKey(): String = BuildConfig.API_KEY


    @Provides
    fun providesContentType(): MediaType = MediaType.get("application/json")


    @Provides
    fun providesJsonConverterFactory(mediaType: MediaType) = json.asConverterFactory(mediaType)

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
        @Named(BASE_URL) baseUrl: String,
        converterFactory: Converter.Factory
    ): Retrofit = Retrofit.Builder().client(okHttpClient).baseUrl(baseUrl)
        .addConverterFactory(converterFactory).build()

    @Provides
    @Named(BASE_URL)
    fun providesBaseUrl() = BuildConfig.BASE_URL

}