package com.softwareforgood.pridefestival.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.softwareforgood.pridefestival.FirstRunCachingService
import com.softwareforgood.pridefestival.databinding.ActivityFirstLoadBinding
import com.softwareforgood.pridefestival.util.HasComponent
import com.softwareforgood.pridefestival.util.makeActivityComponent
import javax.inject.Inject

class FirstLoadActivity : AppCompatActivity(), HasComponent<ActivityComponent> {

    override val component: ActivityComponent by lazy {
        makeActivityComponent()
    }

    @Inject lateinit var firstRunCachingService: FirstRunCachingService

    private lateinit var binding: ActivityFirstLoadBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFirstLoadBinding.inflate(layoutInflater)
        setContentView(binding.root)

        component.inject(this)


    }
}