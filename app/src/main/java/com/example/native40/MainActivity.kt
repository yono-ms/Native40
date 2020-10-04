/*
 * Copyright (c) 2020. yono-ms
 */

package com.example.native40

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.native40.databinding.ActivityMainBinding
import org.slf4j.Logger
import org.slf4j.LoggerFactory

class MainActivity : AppCompatActivity() {

    companion object {
        private val logger: Logger = LoggerFactory.getLogger(MainActivity::class.java.simpleName)
    }

    private val viewModel: MainViewModel by viewModels()

    private val connectivityManager by lazy {
        applicationContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        logger.info("onCreate savedInstanceState=$savedInstanceState")
        val binding: ActivityMainBinding =
            DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.headerText.observe(this, {
            supportActionBar?.title = it
        })
        viewModel.connected.observe(this, Observer {
            supportActionBar?.subtitle =
                if (it) getString(R.string.text_empty) else getString(R.string.text_offline)
        })

        val navController = findNavController(R.id.navHostFragment)
        val appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.home, R.id.start -> supportActionBar?.setDisplayHomeAsUpEnabled(false)
                else -> supportActionBar?.setDisplayHomeAsUpEnabled(true)
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        logger.info("onSupportNavigateUp")
        return findNavController(R.id.navHostFragment).popBackStack() || super.onSupportNavigateUp()
    }

    override fun onStart() {
        super.onStart()
        registerNetworkCallback()
    }

    override fun onStop() {
        super.onStop()
        unregisterNetworkCallback()
    }

    private fun registerNetworkCallback() {
        val request = NetworkRequest.Builder()
            .addCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET)
            .addCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            .build()
        connectivityManager.registerNetworkCallback(request, networkCallback)
    }

    private fun unregisterNetworkCallback() {
        connectivityManager.unregisterNetworkCallback(networkCallback)
    }

    private val networkCallback = object : ConnectivityManager.NetworkCallback() {
        override fun onAvailable(network: Network) {
            logger.info("onAvailable $network")
            checkConnection()
        }

        override fun onLost(network: Network) {
            logger.info("onLost $network")
            checkConnection()
        }
    }

    private fun checkConnection() {
        val activeNetworks = connectivityManager.allNetworks
            .mapNotNull {
                connectivityManager.getNetworkCapabilities(it)
            }.filter {
                it.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) &&
                        it.hasCapability(NetworkCapabilities.NET_CAPABILITY_VALIDATED)
            }
        viewModel.connected.postValue(activeNetworks.isNotEmpty())
    }
}