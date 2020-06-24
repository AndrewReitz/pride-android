package com.softwareforgood.pridefestival

import android.app.Application
import com.parse.Parse
import com.softwareforgood.pridefestival.ui.ActivityComponent
import javax.inject.Scope
import com.f2prateek.rx.preferences2.RxSharedPreferences
import android.preference.PreferenceManager
import com.f2prateek.rx.preferences2.Preference
import dagger.Module
import dagger.Provides
import dagger.Reusable
import javax.inject.Qualifier
import kotlin.annotation.AnnotationTarget.FIELD
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.VALUE_PARAMETER

@Scope annotation class ApplicationScope

interface BasePrideAppComponent {
    val parseConfig: Parse.Configuration
    val firstRunCachingService: FirstRunCachingService
    val initialize: AppInitializer

    fun activityComponentBuilder(): ActivityComponent.Builder

    interface Builder {
        fun application(application: Application): Builder
        fun build(): BasePrideAppComponent
    }
}

@Module
object PrideAppModule {

    @JvmStatic
    @Provides
    @Reusable
    fun provideParseConfig(
        application: Application,
        @ParseServerUrl parseServerUrl: String
    ): Parse.Configuration = Parse.Configuration.Builder(application)
                .applicationId("kl37CbvvFZGY1diRa7hbMG6X3ZQGcsKZEQRpNMHA")
                .server(parseServerUrl)
                .enableLocalDataStore()
                .build()

    @JvmStatic
    @Provides
    @Reusable
    fun provideRxSharedPreferences(application: Application): RxSharedPreferences {
        val sharedPrefs = PreferenceManager.getDefaultSharedPreferences(application)
        return RxSharedPreferences.create(sharedPrefs)
    }

    @JvmStatic
    @Provides
    @Reusable
    @ParseServerUrl
    fun provideParseServerUrl(
        @UseStagingUrlPref useStagingUrl: Preference<Boolean>
    ): String = if (useStagingUrl.get()) "https://pride-festival-parse-staging.herokuapp.com/parse"
    else "https://pride-festival-parse.herokuapp.com/parse"

    /**
     * Allows overrides on debug builds with Stetho.
     */
    @JvmStatic
    @Provides
    @Reusable
    @UseStagingUrlPref
    fun provideUseStagePref(rxPrefs: RxSharedPreferences) = rxPrefs.getBoolean("use-staging-url", false)

    @JvmStatic
    @Provides
    @Reusable
    @FirstRunPref
    fun provideFirstRunPref(rxPrefs: RxSharedPreferences) = rxPrefs.getBoolean("first-run", true)
}

@Qualifier @Target(FIELD, FUNCTION, VALUE_PARAMETER) annotation class UseStagingUrlPref
@Qualifier annotation class FirstRunPref
@Qualifier annotation class ParseServerUrl
