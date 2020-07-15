package com.softwareforgood.pridefestival.ui

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.FragmentManager
import com.softwareforgood.pridefestival.ui.events.EventsComponent
import com.softwareforgood.pridefestival.ui.favorites.FavoritesComponent
import com.softwareforgood.pridefestival.ui.info.InfoComponent
import com.softwareforgood.pridefestival.ui.map.MapComponent
import com.softwareforgood.pridefestival.ui.parade.ParadeComponent
import com.softwareforgood.pridefestival.ui.vendor.VendorComponent
import dagger.BindsInstance
import dagger.Module
import dagger.Provides
import dagger.Subcomponent
import io.reactivex.Single
import javax.inject.Scope

@Scope annotation class ActivityScope

@ActivityScope
@Subcomponent(modules = [ActivityModule::class])
interface ActivityComponent {

    val eventsComponent: EventsComponent
    val paradeComponent: ParadeComponent
    val vendorComponent: VendorComponent
    val mapComponent: MapComponent
    val favoritesComponent: FavoritesComponent
    val infoComponentBuilder: InfoComponent.Builder

    fun inject(activity: MainActivity)
    fun inject(activity: FirstLoadActivity)
    fun inject(deepLinkActivity: DeepLinkActivity)

    @Subcomponent.Builder
    interface Builder {
        @BindsInstance fun searchViewProvider(searchViewProvider: Single<SearchView>): Builder
        @BindsInstance fun activity(activity: AppCompatActivity): Builder

        fun build(): ActivityComponent
    }
}

@Module object ActivityModule {
    @JvmStatic
    @Provides
    fun provideFragmentManager(activity: AppCompatActivity): FragmentManager = activity.supportFragmentManager

    @JvmStatic
    @Provides
    fun provideLayoutInflater(activity: AppCompatActivity): LayoutInflater = LayoutInflater.from(activity)
}
