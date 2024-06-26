package com.movielabs.moviebrowserkmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.arkivanov.decompose.defaultComponentContext
import com.movielabs.moviebrowserkmm.mainView
import decompose.MovieBrowserKmmRootImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = MovieBrowserKmmRootImpl(defaultComponentContext())
        setContent {
            mainView(root)
        }
    }
}