package decompose

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.*
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable
import com.arkivanov.essenty.parcelable.Parcelize

class MovieBrowserKmmRootImpl(
    componentContext: ComponentContext,
    private val mainScreen: (ComponentContext, (movieId: Int) -> Unit) -> MainScreenComponent,
    private val movieDetails: (
        ComponentContext, movieId: Int, onBackPressed: () -> Unit
    ) -> DetailsScreenComponent
) : MovieBrowserKmmRoot, ComponentContext by componentContext {

    constructor(
        componentContext: ComponentContext
    ) : this(componentContext,
        mainScreen = { childContext, onMovieSelected ->
            MainScreenComponentImpl(childContext, onMovieSelected)
        },
        movieDetails = { childContext, movieId, onBackPressed ->
            DetailsScreenComponentImpl(childContext, movieId) {
                onBackPressed.invoke()
            }
        }
    )

    private val navigation = StackNavigation<Configuration>()

    private val stack = childStack(
        source = navigation,
        initialConfiguration = Configuration.Dashboard,
        handleBackButton = true,
        childFactory = ::createChild
    )

    private fun createChild(
        configuration: Configuration,
        componentContext: ComponentContext
    ): MovieBrowserKmmRoot.Child =
        when (configuration) {
            Configuration.Dashboard -> MovieBrowserKmmRoot.Child.MainScreen(
                mainScreen(componentContext, ::onMovieSelected)
            )

            is Configuration.Details -> MovieBrowserKmmRoot.Child.DetailScreen(
                movieDetails(componentContext, configuration.movieId, ::onDetailsScreenBackPressed)
            )
        }

    private fun onMovieSelected(movieId: Int) {
        navigation.push(Configuration.Details(movieId))
    }


    private fun onDetailsScreenBackPressed(){
        navigation.pop()
    }


    override val childStack: Value<ChildStack<*, MovieBrowserKmmRoot.Child>>
        get() = value()

    private fun value() = stack

    private sealed class Configuration : Parcelable {
        @Parcelize
        object Dashboard : Configuration()

        @Parcelize
        data class Details(
            val movieId: Int
        ) : Configuration(), Parcelable
    }
}