package io.github.bokchidevchan.core.network.interceptor

import io.github.bokchidevchan.core.network.NetworkConfig
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.util.concurrent.TimeUnit

class OfflineCacheInterceptor(
    private val isNetworkAvailable: () -> Boolean
) : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()

        if (!isNetworkAvailable()) {
            val cacheControl = CacheControl.Builder()
                .maxStale(NetworkConfig.CACHE_MAX_STALE_DAYS, TimeUnit.DAYS)
                .build()

            request = request.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .cacheControl(cacheControl)
                .build()
        }

        return chain.proceed(request)
    }
}
