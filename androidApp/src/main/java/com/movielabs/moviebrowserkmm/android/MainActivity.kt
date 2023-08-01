package com.movielabs.moviebrowserkmm.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import com.movielabs.moviebrowserkmm.mainView
import com.arkivanov.decompose.defaultComponentContext
import decompose.MovieBrowserKmmRootImpl

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val root = MovieBrowserKmmRootImpl(defaultComponentContext())
        setContent {
            MyApplicationTheme {
                mainView(root)
            }
        }
    }
}

@Composable
fun GreetingView(text: String) {
    Text(text = text)
}

@Preview
@Composable
fun DefaultPreview() {
    MyApplicationTheme {
        GreetingView("Hello, Android!")
    }
}
