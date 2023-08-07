package decompose

import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value

interface MovieBrowserKmmRoot {

    val childStack : Value<ChildStack<*, Child>>

    sealed class Child{

        data class MainScreen(val mainScreenComponent: MainScreenComponent) : Child()

        data class DetailScreen(val detailsScreenComponent: DetailsScreenComponent) : Child()
    }
}