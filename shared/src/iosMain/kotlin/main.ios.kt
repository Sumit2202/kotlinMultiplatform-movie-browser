import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.window.ComposeUIViewController
import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.essenty.lifecycle.LifecycleRegistry
import decompose.MovieBrowserKmmRootImpl

fun MainViewController(lifecycle: LifecycleRegistry) = ComposeUIViewController {
    CompositionLocalProvider(){
        val root = MovieBrowserKmmRootImpl(DefaultComponentContext(lifecycle = lifecycle))
        App(root)
    }
}