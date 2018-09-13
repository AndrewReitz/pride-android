package com.softwareforgood.pridefestival.ui.vendor

import android.content.Context
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.RecyclerView
import com.andrewreitz.velcro.betterviewanimator.BetterViewAnimator
import com.jakewharton.rxbinding2.support.v7.widget.SearchViewQueryTextEvent
import com.softwareforgood.pridefestival.R
import com.softwareforgood.pridefestival.util.activityComponent
import com.softwareforgood.pridefestival.util.horizontalDivider
import com.softwareforgood.pridefestival.util.toSearchEventStream
import io.reactivex.Observable
import io.reactivex.Single
import kotlinx.android.synthetic.main.view_vendor.view.*
import javax.inject.Inject

interface VendorView {
    val recyclerView: RecyclerView
    val tryAgainButton: View
    val searches: Observable<SearchViewQueryTextEvent>
    fun showVendorList()
    fun showError()
    fun showSpinner()
}

class DefaultVendorView(context: Context, attrs: AttributeSet)
    : BetterViewAnimator(context, attrs), VendorView {

    override val recyclerView: RecyclerView get() = vendor_list
    override val tryAgainButton: View get() = vendor_error
    override val searches: Observable<SearchViewQueryTextEvent>
        get() = searchViewProvider.toSearchEventStream()

    @Inject lateinit var presenter: VendorPresenter
    @Inject lateinit var searchViewProvider: Single<SearchView>

    init {
        context.activityComponent.vendorComponent.inject(this)
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        vendor_list.horizontalDivider()
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        presenter.attachView(this)
    }

    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        presenter.detachView()
    }

    override fun showVendorList() {
        displayedChildId = R.id.vendor_list
    }

    override fun showError() {
        displayedChildId = R.id.vendor_error
    }

    override fun showSpinner() {
        displayedChildId = R.id.vendor_spinner
    }
}