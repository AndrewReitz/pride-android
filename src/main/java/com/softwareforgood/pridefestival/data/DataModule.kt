package com.softwareforgood.pridefestival.data

import android.app.Application
import android.content.Context
import androidx.room.Room
import com.f2prateek.rx.preferences2.Preference
import com.f2prateek.rx.preferences2.RxSharedPreferences
import com.softwareforgood.pridefestival.data.room.PrideDao
import com.softwareforgood.pridefestival.data.room.PrideDatabase
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.Reusable
import io.github.roguesdev.hoard.Hoard
import io.github.roguesdev.hoard.Types
import io.github.roguesdev.hoard.rxjava2.RxDepositor
import io.github.roguesdev.hoard.rxjava2.RxHoard2
import io.github.roguesdev.hoard.serialization.MoshiSerializer
import org.threeten.bp.Clock
import org.threeten.bp.Duration
import org.threeten.bp.Instant
import javax.inject.Qualifier

@Qualifier annotation class EventsDepositor
@Qualifier annotation class ParadeDepositor
@Qualifier annotation class VendorDepositor

@Qualifier annotation class EventsLastUpdated
@Qualifier annotation class ParadeLastUpdated
@Qualifier annotation class VendorLastUpdated

@Module abstract class DataModule {
    @Binds abstract fun bindsEventsLoader(loader: DefaultEventsLoader): EventsLoader
    @Binds abstract fun bindsParadeLoader(loader: DefaultParadeLoader): ParadeLoader
    @Binds abstract fun bindsVendorLoader(loader: DefaultVendorLoader): VendorLoader
    @Binds abstract fun bindsInfoLoader(loader: DefaultInfoLoader): InfoLoader

    @Binds abstract fun bindsFavoriteStorage(storage: DefaultFavoritesStorage): FavoritesStorage
    @Binds abstract fun bindsParseDelegate(delegate: DefaultParseDelegate): ParseDelegate

    @Module companion object {

        @JvmStatic
        @Provides
        @Reusable
        @EventsLastUpdated
        fun provideEventPreference(rxSharedPreferences: RxSharedPreferences): Preference<Instant> =
                rxSharedPreferences.getObject("events-last-updated", Instant.MIN, InstantPreferenceConverter)

        @JvmStatic
        @Provides
        @Reusable
        @ParadeLastUpdated
        fun provideParadePreference(rxSharedPreferences: RxSharedPreferences): Preference<Instant> =
                rxSharedPreferences.getObject("parade-last-updated", Instant.MIN, InstantPreferenceConverter)

        @JvmStatic
        @Provides
        @Reusable
        @VendorLastUpdated
        fun provideVendorsPreference(rxSharedPreferences: RxSharedPreferences): Preference<Instant> =
                rxSharedPreferences.getObject("vendors-last-updated", Instant.MIN, InstantPreferenceConverter)

        @JvmStatic
        @Provides
        @Reusable
        fun provideUpdateDuration(): Duration = Duration.ofMinutes(60)

        @JvmStatic
        @Provides
        @Reusable
        fun provideClock(): Clock = Clock.systemDefaultZone()

        @JvmStatic
        @Provides
        @Reusable
        fun provideHoard(app: Application) = RxHoard2(
                Hoard.builder()
                        .rootDirectory(app.getDir("favorites", Context.MODE_PRIVATE))
                        .serialzationAdapter(MoshiSerializer())
                        .build()
        )

        @JvmStatic
        @Provides
        @Reusable
        @EventsDepositor
        fun provideEventsDepositor(hoard: RxHoard2): RxDepositor<Set<String>> =
                hoard.createDepositor<Set<String>>("events",
                        Types.newParameterizedType(Set::class.java, String::class.java)
                )

        @JvmStatic
        @Provides
        @Reusable
        @ParadeDepositor
        fun provideParadeDepositor(hoard: RxHoard2): RxDepositor<Set<String>> =
                hoard.createDepositor<Set<String>>("paradeEvents",
                        Types.newParameterizedType(Set::class.java, String::class.java)
                )

        @JvmStatic
        @Provides
        @Reusable
        @VendorDepositor
        fun provideVendorDepositor(hoard: RxHoard2): RxDepositor<Set<String>> =
                hoard.createDepositor<Set<String>>("vendor",
                        Types.newParameterizedType(Set::class.java, String::class.java)
                )

        @Provides
        @Reusable
        fun provideRoom(app: Application): PrideDatabase = Room.databaseBuilder(
            app,
            PrideDatabase::class.java,
            "pridedb"
        ).build()

        @Provides
        @Reusable
        fun providePrideDao(prideDatabase: PrideDatabase): PrideDao = prideDatabase.prideDao()
    }
}
