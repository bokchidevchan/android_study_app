package io.github.bokchidevchan.core.network.di

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
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
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import java.io.File
import java.util.concurrent.TimeUnit

val networkModule = module {
    single<NetworkConnectivity> {
        NetworkConnectivityImpl(androidContext())
    }

    single {
        Json {
            ignoreUnknownKeys = true
            coerceInputValues = true
            isLenient = true
        }
    }

    single {
        HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
    }

    single {
        CacheInterceptor()
    }

    single {
        val networkConnectivity: NetworkConnectivity = get()
        OfflineCacheInterceptor { networkConnectivity.isNetworkAvailable() }
    }

    single {
        val cacheDir = File(androidContext().cacheDir, "http_cache")
        Cache(cacheDir, NetworkConfig.CACHE_SIZE_MB * 1024 * 1024)
    }

    single {
        OkHttpClient.Builder()
            .cache(get())
            .addInterceptor(get<OfflineCacheInterceptor>())
            .addNetworkInterceptor(get<CacheInterceptor>())
            .addInterceptor(get<HttpLoggingInterceptor>())
            .connectTimeout(NetworkConfig.CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .readTimeout(NetworkConfig.READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .writeTimeout(NetworkConfig.WRITE_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .build()
    }

    single {
        val json: Json = get()
        val contentType = "application/json".toMediaType()

        Retrofit.Builder()
            .baseUrl(NetworkConfig.UPBIT_BASE_URL)
            .client(get())
            .addConverterFactory(json.asConverterFactory(contentType))
            .build()
    }
}
