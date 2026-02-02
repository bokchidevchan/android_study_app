package io.github.bokchidevchan.core.network.interceptor

import io.github.bokchidevchan.core.network.NetworkConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class CacheInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val response = chain.proceed(request)

        val cacheControl = CacheControl.Builder()
            .maxAge(NetworkConfig.CACHE_MAX_AGE_SECONDS, TimeUnit.SECONDS)
            .build()

        return response.newBuilder()
            .removeHeader("Pragma")
            .removeHeader("Cache-Control")
            .header("Cache-Control", cacheControl.toString())
            .build()
    }
}
