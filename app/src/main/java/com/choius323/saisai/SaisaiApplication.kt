package com.choius323.saisai

import android.app.Application
import com.choius323.saisai.data.account.AuthDataStore
import com.choius323.saisai.data.account.SessionManager
import com.choius323.saisai.di.dataModule
import com.choius323.saisai.di.uiModule
import com.choius323.saisai.di.useCaseModule
import com.jakewharton.threetenabp.AndroidThreeTen
import com.kakao.vectormap.KakaoMapSdk
import org.koin.android.ext.android.inject
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.GlobalContext

class SaisaiApplication : Application() {
    private val authDataStore by inject<AuthDataStore>()
    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
        GlobalContext.startKoin {
            androidContext(this@SaisaiApplication)
            modules(uiModule, useCaseModule, dataModule)
        }
        KakaoMapSdk.init(this, BuildConfig.KAKAO_KEY)
        SessionManager.init(authDataStore)
    }
}