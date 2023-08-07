package com.movielabs.moviebrowserkmm

import App
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import decompose.MovieBrowserKmmRoot

@Composable
fun mainView(root: MovieBrowserKmmRoot) = CompositionLocalProvider(){
        App(root)
}