package com.softwareforgood.pridefestival

import android.app.Application
import com.jakewharton.processphoenix.ProcessPhoenix
import com.jakewharton.threetenabp.AndroidThreeTen
import com.parse.Parse
import com.softwareforgood.pridefestival.util.HasComponent
import androidx.core.provider.FontRequest
import androidx.emoji.text.EmojiCompat
import androidx.emoji.text.FontRequestEmojiCompatConfig
import io.reactivex.disposables.Disposables
import io.reactivex.plugins.RxJavaPlugins
import org.threeten.bp.ZoneId
import timber.log.Timber

class PrideApp : Application(), HasComponent<PrideAppComponent> {

    companion object{
        /** Default zone for the app because we are in Minneapolis. */
        val CENTRAL_TIMEZONE: ZoneId by lazy { ZoneId.of(ZoneId.SHORT_IDS["CST"]) }
    }

    private lateinit var _component: PrideAppComponent
    override val component: PrideAppComponent by lazy { _component }

    override fun onCreate() {
        super.onCreate()

        if (ProcessPhoenix.isPhoenixProcess(this)) return

        AndroidThreeTen.init(this)

        _component = DaggerPrideAppComponent.builder()
                .application(this)
                .build()

        component.initialize()

        with(component) {
            Parse.initialize(parseConfig)
            firstRunCachingService.cacheParseObjects()
        }

        val fontRequest = FontRequest(
                "com.google.android.gms.fonts",
                "com.google.android.gms",
                "Noto Color Emoji Compat",
                R.array.com_google_android_gms_fonts_certs
        )

        val config = FontRequestEmojiCompatConfig(this, fontRequest)
        EmojiCompat.init(config)
    }
}
