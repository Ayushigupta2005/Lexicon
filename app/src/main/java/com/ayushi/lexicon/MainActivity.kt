package com.ayushi.lexicon

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.utils.NetworkConnectivityObserver
import com.google.android.material.snackbar.Snackbar
import com.utils.NetworkStatus


class MainActivity : AppCompatActivity() {

    private val networkConnectivityObserver:  NetworkConnectivityObserver by lazy {
        NetworkConnectivityObserver(this)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        // here setKeepOnScreenCondition false so, activity redirect another activity
        // and some api call here
        // if setKeepOnScreenCondition true so, activity code not redirect another activity
        splashScreen.setKeepOnScreenCondition { false }

        val snackbar = Snackbar.make(
            findViewById(android.R.id.content),
            "No Internet Connection",
            Snackbar.LENGTH_INDEFINITE
        ).setAction("WiFi"){
            startActivity(Intent(Settings.ACTION_WIFI_SETTINGS))
        }
        networkConnectivityObserver.observe(this){
            when(it){
                NetworkStatus.Available -> {
                    if(snackbar.isShown){
                        snackbar.dismiss()
                    }
                }
                else -> {
                    snackbar.show()
                }
            }


        }
    }
}