package io.github.bokchidevchan.android_study_app

import android.app.Application
import io.github.bokchidevchan.android_study_app.di.appModule
import io.github.bokchidevchan.core.network.di.networkModule
import io.github.bokchidevchan.data.market.di.dataMarketModule
import io.github.bokchidevchan.domain.market.di.domainMarketModule
import io.github.bokchidevchan.feature.market.di.featureMarketModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class UpbitApp : Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@UpbitApp)
            modules(
                appModule,
                networkModule,
                domainMarketModule,
                dataMarketModule,
                featureMarketModule
            )
        }
    }
}
