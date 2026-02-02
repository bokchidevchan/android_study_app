package io.github.bokchidevchan.core.network.di

import android.content.Context
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import io.github.bokchidevchan.core.network.NetworkConfig
import io.github.bokchidevchan.core.network.NetworkConnectivity
import io.github.bokchidevchan.core.network.NetworkConnectivityImpl
import io.github.bokchidevchan.core.network.interceptor.CacheInterceptor
import io.github.bokchidevchan.core.network.interceptor.OfflineCacheInterceptor
import kotlinx.serialization.json.Json
import okhttp3.Cache
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @Singleton
    fun provideNetworkConnectivity(
        @ApplicationContext context: Context
    ): NetworkConnectivity {
        return NetworkConnectivityImpl(context)
    }

    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    @Provides
    @Singleton
    fun provideHttpLoggingInterceptor(): HttpLoggingInterceptor {
        return HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    @Provides
    @Singleton
    fun provideCacheInterceptor(): CacheInterceptor {
        return CacheInterceptor()
    }

    @Provides
    @Singleton
    fun provideOfflineCacheInterceptor(
        networkConnectivity: NetworkConnectivity
    ): OfflineCacheInterceptor {
        return OfflineCacheInterceptor { networkConnectivity.isNetworkAvailable() }
    }

    @Provides
    @Singleton
    fun provideCache(
        @ApplicationContext context: Context
    ): Cache {
        val cacheDir = File(context.cacheDir, "http_cache")
        return Cache(cacheDir, NetworkConfig.CACHE_SIZE_MB * 1024 * 1024)
    }

    @Provides
    @Singleton
    fun provideOkHttpClient(
        cache: Cache,
        offlineCacheInterceptor: OfflineCacheInterceptor,
        cacheInterceptor: CacheInterceptor,
        loggingInterceptor: HttpLoggingInterceptor
    ): OkHttpClient {
        return OkHttpClient.Builder()
            .cache(cache)
            .addInterceptor(offlineCacheInterceptor)
            .addNetworkInterceptor(cacheInterceptor)
            .addInterceptor(loggingInterceptor)
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        json: Json,
        okHttpClient: OkHttpClient
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl(NetworkConfig.UPBIT_BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
